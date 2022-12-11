terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "4.40.0"
    }
  }
  # backend "s3" {
  #   bucket = "pgr301-2022-terraform-state"
  #   key    = "1045/terraform-in-pipeline.state"
  #   region = "eu-west-1"
  # }

}
provider "aws" {
    region = "eu-west-1"
}