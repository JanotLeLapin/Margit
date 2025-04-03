# Margit

A Minecraft 1.8.8 server modding toolchain

## Description

Margit is a set of tools and patches that modify the official Minecraft 1.8.8 server jar. Notable changes include:

- implement Bukkit API
- use latest dependencies
- use JDK 23

## Usage

Minecraft [EULA](https://www.minecraft.net/en-us/eula) says we can't host decompiled proprietary source coude here on Github. Instead, Margit relies on the [Nix](https://nixos.org/) package manager. To set up your development environment simply install Nix on your system (consider using WSL2 if you're on Windows) then run:

```sh
git clone https://github.com/JanotLeLapin/Margit
cd Margit
nix develop
```

You should now have deobfuscated, decompiled and patched `api` and `server` codebases. You may now make changes, commit them, and rebuild patches using the `margit-build-patches` command.
