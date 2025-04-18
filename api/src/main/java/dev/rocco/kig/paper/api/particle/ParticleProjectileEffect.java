package dev.rocco.kig.paper.api.particle;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ParticleProjectileEffect extends ProjectileEffect {
    private Effect effect;
    private int id, data, count, radius = 32;
    private float offsetX, offsetY, offsetZ, speed;

    @Override
    public void spawn(Location loc) {
        spawn(loc, null);
    }

    public void spawn(Location loc, Player emitter) {
        loc.getWorld().spigot().playEffect(loc, effect, id, data, offsetX, offsetY, offsetZ, speed, count, radius, emitter);
    }

    private ParticleProjectileEffect() {
        super(2);
    }

    public static class Builder {
        private Effect effect;
        private int id, data, count, radius = 32;
        private float offsetX, offsetY, offsetZ, speed;

        public Builder(Effect effect) {
            this.effect = effect;
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setData(int data) {
            this.data = data;
            return this;
        }

        public Builder setCount(int count) {
            this.count = count;
            return this;
        }

        public Builder setRadius(int radius) {
            this.radius = radius;
            return this;
        }

        public Builder setOffsetX(float offsetX) {
            this.offsetX = offsetX;
            return this;
        }

        public Builder setOffsetY(float offsetY) {
            this.offsetY = offsetY;
            return this;
        }

        public Builder setOffsetZ(float offsetZ) {
            this.offsetZ = offsetZ;
            return this;
        }

        public Builder setSpeed(float speed) {
            this.speed = speed;
            return this;
        }

        public ParticleProjectileEffect build() {
            ParticleProjectileEffect result = new ParticleProjectileEffect();
            result.effect = effect;
            result.id = id;
            result.radius = radius;
            result.count = count;
            result.speed = speed;
            result.offsetX = offsetX;
            result.offsetY = offsetY;
            result.offsetZ = offsetZ;
            result.data = data;
            return result;
        }
    }
}
