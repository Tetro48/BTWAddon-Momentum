package net.tetro48.momentum;

public interface MomentumAffected {
	int momentum$getBlocksBroken();
	void momentum$incrementBlocksBroken();
	void momentum$decayBlocksBroken();
	void momentum$resetBlocksBroken();
	void momentum$tickDecay();
}
