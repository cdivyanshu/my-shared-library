# FRONTEND

resource "aws_security_group" "frontend_security_group" {
  vpc_id = aws_vpc.ot_microservices_dev.id
  name = "frontend-security-group"

  tags = {
    Name = "frontend-security-group"
  }
  
  ingress {
    from_port        = 3000
    to_port          = 3000
    protocol         = "tcp"
    security_groups = [aws_security_group.alb_security_group.id]
  }

  ingress {
    from_port        = 22
    to_port          = 22
    protocol         = "tcp"
    security_groups = [aws_security_group.bastion_security_group.id]
  }

  egress {
    from_port        = 0
    to_port          = 0
    protocol         = "-1"
    cidr_blocks      = ["0.0.0.0/0"]
    # ipv6_cidr_blocks = ["::/0"]
  }
}


# frontend instance

resource "aws_instance" "frontend_instance" {
  # ami to be replaced
  ami           = "ami-05eb8291d90fc00c8"
  subnet_id = aws_subnet.frontend_subnet.id
  key_name = "devinfra"
  vpc_security_group_ids = [ aws_security_group.frontend_security_group.id ]
  instance_type = "t2.micro"

  tags = {
    Name = "Frontend"
  }
}

# target group and attachment

resource "aws_lb_target_group" "frotend_target_group" {
  name     = "tf-example-lb-tg"
  port     = 80
  protocol = "HTTP"
  target_type = "instance"
  vpc_id   = aws_vpc.ot_microservices_dev.id
}

resource "aws_lb_target_group_attachment" "frontend_target_group_attachment" {
  target_group_arn = aws_lb_target_group.frotend_target_group.arn
  target_id        = aws_instance.frontend_instance.id
  port             = 3000
}

# load balancer

resource "aws_lb" "test" {
  name               = "frontend-lb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.alb_security_group.id]
  subnets            = [aws_subnet.public_subnet_1.id , aws_subnet.public_subnet_2.id]
}

resource "aws_lb_listener" "front_end" {
  load_balancer_arn = aws_lb.test.arn
  port              = "80"
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.frotend_target_group.arn
  }
}




# launch template

resource "aws_launch_template" "frontend_launch_template" {
  name = "frontend-template"

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

  key_name      = "devinfra"
  # ami to be replaced with actual ami currently not right
  image_id      = "ami-05eb8291d90fc00c8"
  instance_type = "t2.micro"

  tag_specifications {
    resource_type = "instance"

    tags = {
      Name = "FrontendASG"
    }
  }
}

# auto scaling

resource "aws_autoscaling_group" "frontend_autoscaling" {
  name                      = "frontend-autoscale"
  max_size                  = 2
  min_size                  = 0
  desired_capacity = 0
  health_check_grace_period = 300
  launch_template {
    id      = aws_launch_template.frontend_launch_template.id
    version = "$Default"
  }
  vpc_zone_identifier = [aws_subnet.frontend_subnet.id]
  target_group_arns = [aws_lb_target_group.frotend_target_group.arn]
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

