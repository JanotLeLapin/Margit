{
  description = "A Minecraft server modification";

  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs?ref=nixos-unstable";
    overlay.url = "github:oxalica/rust-overlay";
  };

  outputs = {
    self,
    nixpkgs,
    overlay,
    ...
  }: let
    eachSystem = fn: nixpkgs.lib.genAttrs [
      "x86_64-linux"
      "aarch64-linux"
    ] (system: (fn {
      inherit system;
      pkgs = (import nixpkgs {
        inherit system;
        overlays = [ (import overlay) ];
      });
    }));
  in {
    devShells = eachSystem ({ pkgs, ... }: { default = pkgs.callPackage ./shell.nix {}; });
  };
}
