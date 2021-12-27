#!/bin/bash

echo "[TASK 1] Update OS"
sudo apt-get update

echo "[TASK 2] Install utility tools"
sudo apt-get install software-properties-common -y

echo "[TASK 3] Add ansible to the repository"
sudo apt-add-repository --yes --update ppa:ansible/ansible

echo "[TASK 4] Install Ansible"
sudo apt-get install ansible -y

echo "[TASK 5] Verify Installation"
ansible --version