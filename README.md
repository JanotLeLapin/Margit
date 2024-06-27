# Margit

A Minecraft 1.8.8 server modding toolchain

## Description

Margit is a set of tools and patches that modify the official Minecraft 1.8.8 server jar. Notable changes include:

- use latest dependencies
- use JDK 22
- use lightweight green threads
- console tab completer
- remove Mojang snooper

## Usage

Minecraft [EULA](https://www.minecraft.net/en-us/eula) says we can't host decompiled proprietary source coude here on Github. Instead, Margit relies on the [Nix](https://nixos.org/) package manager. To set up your development environment simply install Nix on your system (consider using WSL2 if you're on Windows) then run:

```sh
git clone https://github.com/JanotLeLapin/Margit
cd Margit
nix develop
```

You should now have a deobfuscated, decompiled and patched `src` directory, cd into it and write some code!

You might notice that `src` is a local Git repository. That's because we use its commit history to generate [patches](./patches). If you want to generate a patch, you simply need to write some code, commit it to `src`, and use the special `margit-build-patches` command from the root of this repository.
