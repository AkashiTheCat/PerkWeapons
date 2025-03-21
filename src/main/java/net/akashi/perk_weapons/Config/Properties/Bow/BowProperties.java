package net.akashi.perk_weapons.Config.Properties.Bow;

import net.minecraftforge.common.ForgeConfigSpec;

public class BowProperties {
	public ForgeConfigSpec.IntValue DRAW_TIME;
	public ForgeConfigSpec.DoubleValue DAMAGE;
	public ForgeConfigSpec.DoubleValue VELOCITY;
	public ForgeConfigSpec.DoubleValue INACCURACY;
	public ForgeConfigSpec.DoubleValue SPEED_MODIFIER;
	public ForgeConfigSpec.DoubleValue ZOOM_FACTOR;
	public ForgeConfigSpec.BooleanValue ONLY_MAINHAND;

	public BowProperties(ForgeConfigSpec.Builder builder, String name,
	                     int defaultDrawTime, double defaultDamage,
	                     double defaultVelocity, double defaultInaccuracy,
	                     double defaultSpeedModifier, double defaultZoomFactor,
	                     boolean onlyAllowMainHand, boolean shouldPop) {
		builder.push(name);
		DRAW_TIME = builder.comment("Draw Time Of " + name + " In Ticks")
				.defineInRange("DrawTime", defaultDrawTime, 0, Integer.MAX_VALUE);
		DAMAGE = builder.comment("Damage Of " + name)
				.defineInRange("Damage", defaultDamage, 0, Double.MAX_VALUE);
		VELOCITY = builder.comment("Projectile Velocity Of " + name)
				.defineInRange("Velocity", defaultVelocity, 0.1, 20.0);
		INACCURACY = builder.comment("Inaccuracy Of Arrow")
				.defineInRange("Inaccuracy", defaultInaccuracy, 0.0, 20.0);
		SPEED_MODIFIER = builder.comment("Speed Modifier When Holding " + name)
				.defineInRange("SpeedModifier", defaultSpeedModifier, -1.0, 1.0);
		ZOOM_FACTOR = builder.comment("Zoom Factor Of " + name + " When Aiming")
				.comment("The Higher The Value is, The Higher The Zooming Level Will Be")
				.defineInRange("ZoomFactor", defaultZoomFactor, 0.0, 1.0);
		ONLY_MAINHAND = builder.comment("Set True To Make " + name + " Only Can Be Used In Main Hand.")
				.comment("This Will Be Automatically Set True If SpeedModifier!=0 To Avoid A Bug Caused By The " +
						"Vanilla Equipment Update Method")
				.define("onlyMainHand", onlyAllowMainHand);
		if (shouldPop) {
			builder.pop();
		}
	}
}
