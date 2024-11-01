package net.akashi.perk_weapons.Entities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.level.Level;

public class Hound extends Wolf {
	private int lifeTime;

	public Hound(EntityType<? extends Wolf> pEntityType, Level pLevel, int LifeTime) {
		super(pEntityType, pLevel);
		this.lifeTime = LifeTime;
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.level().isClientSide()) {
			if (lifeTime > 0) {
				lifeTime--;
			} else {
				this.discard();
			}
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		this.lifeTime = pCompound.getInt("lifeTime");
	}

	@Override
	public void addAdditionalSaveData(CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		pCompound.putInt("lifeTime", this.lifeTime);
	}
}
