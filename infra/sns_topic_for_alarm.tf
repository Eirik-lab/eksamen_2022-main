resource "aws_sns_topic" "alarms" {
  name = "alarm-topic-${var.candidate_id}"
}

resource "aws_sns_topic_subscription" "user_updates_sqs_target" {
  topic_arn = aws_sns_topic.alarms.arn
  protocol  = "email"
  endpoint  = var.candidate_email
}

resource "aws_cloudwatch_metric_alarm" "x300sectreshold" {
  alarm_name                = "cart-afk-time-must-be-5-min"
  namespace                 = var.candidate_id
  metric_name               = "carts.value"

  comparison_operator       = "GreaterThanThreshold"
  threshold                 = "5"
  evaluation_periods        = "2"
  period                    = "300"

  statistic                 = "Maximum"

  alarm_description         = "This alarm goes off as soon as the cart is idle for more than 5 minutes"
  insufficient_data_actions = []
  alarm_actions       = [aws_sns_topic.alarms.arn]
}

