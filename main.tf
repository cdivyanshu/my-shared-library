# Define the AWS provider
provider "aws" {
  region = "ap-south-1"  # Updated region to ap-south-1
}

# Define the VPC
resource "aws_vpc" "ot_microservices_dev" {
  cidr_block = "10.0.0.0/16"
  enable_dns_support = true
  enable_dns_hostnames = true

  tags = {
    Name = "ot_microservices_dev"
  }
}

# Define the Internet Gateway
resource "aws_internet_gateway" "main" {
  vpc_id = aws_vpc.ot_microservices_dev.id

  tags = {
    Name = "main"
  }
}

# Define the Security Groups
resource "aws_security_group" "frontend_security_group" {
  vpc_id = aws_vpc.ot_microservices_dev.id
  name   = "frontend-security-group"

  tags = {
    Name = "frontend-security-group"
  }

  ingress {
    from_port        = 3000
    to_port          = 3000
    protocol         = "tcp"
    cidr_blocks      = ["0.0.0.0/0"]
  }

  ingress {
    from_port        = 22
    to_port          = 22
    protocol         = "tcp"
    cidr_blocks      = ["0.0.0.0/0"]
  }

  egress {
    from_port        = 0
    to_port          = 0
    protocol         = "-1"
    cidr_blocks      = ["0.0.0.0/0"]
  }
}

# Define the Subnets
resource "aws_subnet" "frontend_subnet" {
  vpc_id            = aws_vpc.ot_microservices_dev.id
  cidr_block        = "10.0.1.0/24"
  availability_zone = "ap-south-1a"  # Updated availability zone

  tags = {
    Name = "frontend-subnet"
  }
}

resource "aws_subnet" "public_subnet_1" {
  vpc_id            = aws_vpc.ot_microservices_dev.id
  cidr_block        = "10.0.2.0/24"
  availability_zone = "ap-south-1a"  # Updated availability zone

  tags = {
    Name = "public-subnet-1"
  }
}

resource "aws_subnet" "public_subnet_2" {
  vpc_id            = aws_vpc.ot_microservices_dev.id
  cidr_block        = "10.0.3.0/24"
  availability_zone = "ap-south-1b"  # Updated availability zone

  tags = {
    Name = "public-subnet-2"
  }
}

# Define the Instances
resource "aws_instance" "frontend_instance" {
  ami           = "ami-0ad21ae1d0696ad58"  # Updated AMI ID
  subnet_id     = aws_subnet.frontend_subnet.id
  key_name       = "devinfra"  # Ensure this key pair exists
  vpc_security_group_ids = [aws_security_group.frontend_security_group.id]
  instance_type = "t2.micro"

  tags = {
    Name = "Frontend"
  }
}

# Define the Load Balancer Target Group
resource "aws_lb_target_group" "frontend_target_group" {
  name     = "frontend-aws-tg"  # Changed name
  port     = 80
  protocol = "HTTP"
  target_type = "instance"
  vpc_id   = aws_vpc.ot_microservices_dev.id
}

resource "aws_lb_target_group_attachment" "frontend_target_group_attachment" {
  target_group_arn = aws_lb_target_group.frontend_target_group.arn
  target_id        = aws_instance.frontend_instance.id
  port             = 3000
}

# Define the Load Balancer with a new name
resource "aws_lb" "frontend_aws_lb" {  # Changed name
  name               = "frontend-aws-lb"  # Changed name to avoid conflict
  internal           = false
  load_balancer_type = "application"
  subnets            = [aws_subnet.public_subnet_1.id, aws_subnet.public_subnet_2.id]
  security_groups    = [aws_security_group.frontend_security_group.id]
}

resource "aws_lb_listener" "frontend_listener" {
  load_balancer_arn = aws_lb.frontend_aws_lb.arn
  port              = "80"
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.frontend_target_group.arn
  }
}

# Define the Launch Template with updated name
resource "aws_launch_template" "frontend_aws_launch_template" {  # Changed name
  name = "frontend-aws-template"  # Changed name to avoid conflict

  block_device_mappings {
    device_name = "/dev/sdf"

    ebs {
      volume_size = 10
      volume_type = "gp3"
    }
  }

  network_interfaces {
    subnet_id                   = aws_subnet.frontend_subnet.id
    associate_public_ip_address = false
    security_groups             = [aws_security_group.frontend_security_group.id]
  }

  key_name      = "devinfra"  # Ensure this matches the key pair name
  image_id      = "ami-0ad21ae1d0696ad58"  # Updated AMI ID
  instance_type = "t2.micro"

  tag_specifications {
    resource_type = "instance"

    tags = {
      Name = "FrontendASG"
    }
  }
}

# Define the Auto Scaling Group
resource "aws_autoscaling_group" "frontend_autoscaling" {
  name                      = "frontend-autoscale"
  max_size                  = 2
  min_size                  = 0
  desired_capacity          = 0
  health_check_grace_period = 300
  launch_template {
    id      = aws_launch_template.frontend_aws_launch_template.id  # Updated reference
    version = "$Default"
  }
  vpc_zone_identifier = [aws_subnet.frontend_subnet.id]
  target_group_arns = [aws_lb_target_group.frontend_target_group.arn]
}

resource "aws_autoscaling_policy" "frontend_autoscaling_policy" {
  name                   = "frontend-autoscaling-policy"
  policy_type            = "TargetTrackingScaling"
  adjustment_type        = "ChangeInCapacity"
  estimated_instance_warmup = 300
  autoscaling_group_name = aws_autoscaling_group.frontend_autoscaling.name

  target_tracking_configuration {
    predefined_metric_specification {
      predefined_metric_type = "ASGAverageCPUUtilization"
    }

    target_value = 50.0
  }
}
