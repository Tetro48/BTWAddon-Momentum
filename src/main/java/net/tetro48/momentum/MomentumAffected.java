package net.tetro48.momentum;

public interface MomentumAffected {
	int momentum$getBlocksBroken();
	void momentum$setBlockID(int blockID);
	void momentum$incrementBlocksBroken();
	void momentum$resetBlocksBroken();
}
