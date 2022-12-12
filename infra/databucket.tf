# Jim; this just fails ... commented it out ! We need to figure this out later, starting new task instead...

resource "aws_s3_bucket" "analyticsbucket" {
  bucket = "analytics-${var.candidate_id}"
    acl    = "private"
    tags = {
      Name = "analytics-${var.candidate_id}"
    }
    versioning {
      enabled = true
    }
    lifecycle_rule {
      id      = "log"
      enabled = true
      prefix  = "log/"
      tags = {
        autoclean = "true"
        rule      = "log"
      }
      expiration {
        days = 90
      }
    }
    lifecycle_rule {
      id      = "tmp"
      enabled = true
      prefix  = "tmp/"
      tags = {
        autoclean = "true"
        rule      = "tmp"
      }
      expiration {
        days = 1
      }
    }
#  acl = "public-read"
#  policy = <<EOF
#{
#  "Id": "bucket_policy_site",
#  "Version": "2012-10-17",
#  "Statement": [
#    {
#      "Sid": "bucket_policy_site_main",
#      "Action": [
#        "s3:GetObject"
#      ],
#      "Effect": "Allow",
#      "Resource": "arn:aws:s3:::analytics-${var.candidate_id}/*",
#      "Principal": "*"
#    }
#  ]
#}
#EOF

}
