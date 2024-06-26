# Margit

A Minecraft 1.8.8 server modding toolchain

## Description

Margit is a set of tools and patches that modify the official Minecraft 1.8.8 server jar. Notable changes include:

- use latest dependencies
- use JDK 22
- use lightweight green threads
- remove Mojang snooper

## Usage

Margit relies on the [Nix](https://nixos.org/) package manager, to set up your development environment simply install Nix on your system (consider using WSL2 if you're on Windows) then run:

```sh
git clone https://github.com/JanotLeLapin/Margit
cd Margit
nix develop
```

You should now have a deobfuscated, decompiled and patched `src` directory.
