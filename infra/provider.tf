terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "4.40.0"
    }
  }
   backend "s3" {
     bucket = "analytics-1045"
     key    = "1045/terraform-in-pipeline.state"
     region = "eu-west-1"
   }

}
#provider "aws" {
#    region = "eu-west-1"
#}