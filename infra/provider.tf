terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "4.40.0"
    }
  }
  backend "s3" {
    bucket = "pgr301-2022-terraform-state"
    key    = "${var.candidate_id}/terraform-in-pipeline.state"
    region = "eu-west-1"
  }

}