#!/bin/bash

# Getting directory of the script to run it independant of current working directory.
parent_path=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )
cd "$parent_path"

echo "Activating githooks..."
echo "################################"

echo "Copying hooks in .githooks/ to .git/hooks/..."
cp ./scripts/* ../.git/hooks/
echo "################################"

echo "Editing the permissions of the file to make it executable..."
chmod +x ../.git/hooks/*
echo "################################"
