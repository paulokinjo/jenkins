# Ansible Jenkins Configuration Management

# Vagrant
```
$ vagrant up
```

# Ansible
```
$ vagrant ssh acs

$ git clone https://github.com/paulokinjo/jenkins

$ cd ansible 

$ ./install_ansible.sh
```

# Creating the secret
```
$ ansible-vault encrypt_string --vault-id vagrant@prompt 'vagrant' --name 'ansible_ssh_pass'

ansible_ssh_pass: !vault |
          $ANSIBLE_VAULT;1.2;AES256;vagrant
          65336136383730613162313536653838353139623365343737323339613435303130313232373061
          3239323830316432623963313132653833313661343434650a363564643563663464303538383333
          61643339323339313338646230303633313334383662623232303063653963356261653733336164
          6661363337373564620a613733663733643764333736386162313635623437666363666532393663
          6161          
```

Update the ```inventory.yaml``` file

```
$ ansible-playbook start.yaml --ask-vault-pass
```