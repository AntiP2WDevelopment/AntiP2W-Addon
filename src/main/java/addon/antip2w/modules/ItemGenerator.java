package addon.antip2w.modules;

import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ItemGenerator extends Module {
    public ItemGenerator() {
        super(Categories.DEFAULT, "ItemGenerator", "Generates items as you like.");
    }

    private final SettingGroup sgGeneral = settings.createGroup("Main");
    private final SettingGroup sgName = settings.createGroup("Name");
    private final SettingGroup sgEnchants = settings.createGroup("Enchants");
    private final SettingGroup sgExtras = settings.createGroup("Extras");
    private final SettingGroup sgAttributes = settings.createGroup("Attributes");

    private final Setting<List<Item>> item = sgGeneral.add(new ItemListSetting.Builder()
        .name("Item")
        .description("Lorem Ipsum")
        .build()
    );

    private final Setting<Integer> ItemCount = sgGeneral.add(new IntSetting.Builder()
        .name("Item Count")
        .description("ItemCount")
        .defaultValue(1)
        .min(-1)
        .sliderMin(1)
        .sliderMax(1024)
        .build()
    );


    //Name
    private final Setting<Boolean> CustomName = sgName.add(new BoolSetting.Builder()
        .name("CustomName")
        .description("If to place the frame back")
        .defaultValue(false)
        .build()
    );

    private final Setting<String> CustomNameString = sgName.add(new StringSetting.Builder()
        .name("CustomName String")
        .description("sell comamnd to sell to ah")
        .defaultValue("Lorem Ipsum")
        .visible(CustomName::get)
        .build()
    );

    private final Setting<Boolean> CustomNameItalic = sgName.add(new BoolSetting.Builder()
        .name("CustomNameItalic")
        .description("If to place the frame back")
        .defaultValue(false)
        .visible(CustomName::get)
        .build()
    );

    private final Setting<Boolean> CustomNameBold = sgName.add(new BoolSetting.Builder()
        .name("CustomNameBold")
        .description("If to place the frame back")
        .defaultValue(false)
        .visible(CustomName::get)
        .build()
    );

    private final Setting<Boolean> CustomNameUnderlined = sgName.add(new BoolSetting.Builder()
        .name("CustomNameUnderlined")
        .description("If to place the frame back")
        .defaultValue(false)
        .visible(CustomName::get)
        .build()
    );

    private final Setting<Boolean> CustomNameStriketrough = sgName.add(new BoolSetting.Builder()
        .name("CustomNameStriketrough")
        .description("If to place the frame back")
        .defaultValue(false)
        .visible(CustomName::get)
        .build()
    );

    private final Setting<Boolean> CustomNameColor = sgName.add(new BoolSetting.Builder()
        .name("CustomNameColor?")
        .description("If to place the frame back")
        .defaultValue(false)
        .visible(CustomName::get)
        .build()
    );

    private final Setting<ItemGenerator.NameColor> CustomNameChoosenColor = sgName.add(new EnumSetting.Builder<ItemGenerator.NameColor>()
        .name("Choose a color")
        .defaultValue(NameColor.gold)
        .visible(CustomName::get)
        .build()
    );

    //Lore
    private final Setting<Boolean> CustomLore = sgName.add(new BoolSetting.Builder()
        .name("CustomLore")
        .description("If to place the frame back")
        .defaultValue(false)
        .build()
    );

    private final Setting<String> CustomLoreString = sgName.add(new StringSetting.Builder()
        .name("CustomLore String")
        .description("sell comamnd to sell to ah")
        .defaultValue("Lorem Ipsum")
        .visible(CustomLore::get)
        .build()
    );

    private final Setting<Boolean> CustomLoreItalic = sgName.add(new BoolSetting.Builder()
        .name("CustomLoreItalic")
        .description("If to place the frame back")
        .defaultValue(false)
        .visible(CustomLore::get)
        .build()
    );

    private final Setting<Boolean> CustomLoreBold = sgName.add(new BoolSetting.Builder()
        .name("CustomLoreBold")
        .description("If to place the frame back")
        .defaultValue(false)
        .visible(CustomLore::get)
        .build()
    );

    private final Setting<Boolean> CustomLoreUnderlined = sgName.add(new BoolSetting.Builder()
        .name("CustomLoreUnderlined")
        .description("If to place the frame back")
        .defaultValue(false)
        .visible(CustomLore::get)
        .build()
    );

    private final Setting<Boolean> CustomLoreStriketrough = sgName.add(new BoolSetting.Builder()
        .name("CustomLoreStriketrough")
        .description("If to place the frame back")
        .defaultValue(false)
        .visible(CustomLore::get)
        .build()
    );

    private final Setting<Boolean> CustomLoreColor = sgName.add(new BoolSetting.Builder()
        .name("CustomLoreColor")
        .description("If to place the frame back")
        .defaultValue(false)
        .visible(CustomLore::get)
        .build()
    );

    private final Setting<ItemGenerator.LoreColor> CustomLoreChoosenColor = sgName.add(new EnumSetting.Builder<ItemGenerator.LoreColor>()
        .name("Choose a color")
        .defaultValue(LoreColor.gold)
        .visible(CustomLore::get)
        .build()
    );


    //Enchants
    private final Setting<Boolean> Enchantment = sgEnchants.add(new BoolSetting.Builder()
        .name("Enchantment?!")
        .description("If to place the frame back")
        .defaultValue(true)
        .build()
    );

    private final Setting<List<Enchantment>> EnchList = sgEnchants.add(new EnchantmentListSetting.Builder()
        .name("Enchantments")
        .description("Select enchantments from the menu with levels")
        .defaultValue(new ArrayList<>())
        .visible(Enchantment::get)
        .build()
    );

    private final Setting<Integer> EnchInt = sgEnchants.add(new IntSetting.Builder()
        .name("Enchantment value")
        .min(1)
        .sliderMin(-255)
        .defaultValue(1)
        .sliderMax(255)
        .visible(Enchantment::get)
        .build()
    );


    //Extras
    private final Setting<Boolean> Extras = sgExtras.add(new BoolSetting.Builder()
        .name("Extras")
        .description("If to place the frame back")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> HideFlags = sgExtras.add(new BoolSetting.Builder()
        .name("HideFlags")
        .description("If to place the frame back")
        .defaultValue(false)
        .visible(Extras::get)
        .build()
    );

    private final Setting<Integer> HideFlagsNum = sgExtras.add(new IntSetting.Builder()
        .name("How much Flags?")
        .description("asd")
        .sliderMin(0)
        .sliderMax(1024)
        .defaultValue(1)
        .visible(Extras::get)
        .build()
    );

    private final Setting<Boolean> Unbreakable = sgExtras.add(new BoolSetting.Builder()
        .name("Unbreakable")
        .description("If to place the frame back")
        .defaultValue(false)
        .visible(Extras::get)
        .build()
    );

    private final Setting<Boolean> Invisible = sgExtras.add(new BoolSetting.Builder()
        .name("Invisible")
        .description("If to place the frame back")
        .defaultValue(false)
        .visible(Extras::get)
        .build()
    );

    private final Setting<Boolean> DyeColor = sgExtras.add(new BoolSetting.Builder()
        .name("DyeColor?")
        .description("If to place the frame back")
        .defaultValue(false)
        .visible(Extras::get)
        .build()
    );

    private final Setting<SettingColor> DyeColorString = sgExtras.add(new ColorSetting.Builder()
        .name("Color")
        .defaultValue(new SettingColor(255, 0, 0, 255))
        .visible(Extras::get)
        .build()
    );

    private final Setting<Boolean> CustomDamage = sgExtras.add(new BoolSetting.Builder()
        .name("CustomDamaged")
        .description("If to place the frame back")
        .defaultValue(false)
        .visible(Extras::get)
        .build()
    );

    private final Setting<String> Damage = sgExtras.add(new StringSetting.Builder()
        .name("Damaged")
        .description("sell comamnd to sell to ah")
        .defaultValue("69420")
        .visible(Extras::get)
        .build()
    );

    private final Setting<Boolean> CustomRepqairCost = sgExtras.add(new BoolSetting.Builder()
        .name("CustomRepqairCost")
        .description("If to place the frame back")
        .defaultValue(false)
        .visible(Extras::get)
        .build()
    );

    private final Setting<String> RepairCost = sgExtras.add(new StringSetting.Builder()
        .name("RepairCost")
        .description("sell comamnd to sell to ah")
        .defaultValue("0")
        .visible(Extras::get)
        .build()
    );


    //Attributes
    private final Setting<Boolean> Attributes = sgAttributes.add(new BoolSetting.Builder()
        .name("Attributes")
        .description("If to place the frame back")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> Armor = sgAttributes.add(new BoolSetting.Builder()
        .name("generic.armor")
        .description("If to place the frame back")
        .defaultValue(false)
        .visible(Attributes::get)
        .build()
    );

    private final Setting<ItemGenerator.ArmorSlot> ArmorSlotType = sgAttributes.add(new EnumSetting.Builder<ItemGenerator.ArmorSlot>()
        .name("armor.slot")
        .defaultValue(ArmorSlot.AnySlot)
        .visible(Armor::get)
        .build()
    );

    private final Setting<Integer> ArmorAmount = sgAttributes.add(new IntSetting.Builder()
        .name("armor.amount")
        .description("asd")
        .sliderMin(0)
        .sliderMax(2147483647)
        .max(69420)
        .defaultValue(1)
        .visible(Armor::get)
        .build()
    );

    private final Setting<Boolean> ArmorToughness = sgAttributes.add(new BoolSetting.Builder()
        .name("generic.armor_toughness")
        .description("If to place the frame back")
        .defaultValue(false)
        .visible(Attributes::get)
        .build()
    );

    private final Setting<ItemGenerator.ArmorToughnessSlot> ArmorToughnessSlotType = sgAttributes.add(new EnumSetting.Builder<ItemGenerator.ArmorToughnessSlot>()
        .name("armor_toughness.slot")
        .defaultValue(ArmorToughnessSlot.AnySlot)
        .visible(ArmorToughness::get)
        .build()
    );

    private final Setting<Integer> ArmorToughnessAmount = sgAttributes.add(new IntSetting.Builder()
        .name("armor_toughness.amount")
        .description("asd")
        .sliderMin(0)
        .sliderMax(2147483647)
        .defaultValue(1)
        .visible(ArmorToughness::get)
        .build()
    );

    private final Setting<Boolean> AttackDamage = sgAttributes.add(new BoolSetting.Builder()
        .name("generic.attack_damage")
        .description("If to place the frame back")
        .defaultValue(false)
        .visible(Attributes::get)
        .build()
    );

    private final Setting<ItemGenerator.AttackDamageSlot> AttackDamageSlotType = sgAttributes.add(new EnumSetting.Builder<ItemGenerator.AttackDamageSlot>()
        .name("attack_damage.slot")
        .defaultValue(AttackDamageSlot.AnySlot)
        .visible(AttackDamage::get)
        .build()
    );

    private final Setting<Integer> AttackDamageAmount = sgAttributes.add(new IntSetting.Builder()
        .name("attack_damage.amount")
        .description("asd")
        .sliderMin(0)
        .sliderMax(2147483647)
        .defaultValue(1)
        .visible(AttackDamage::get)
        .build()
    );

    private final Setting<Boolean> AttackKnockback = sgAttributes.add(new BoolSetting.Builder()
        .name("generic.attack_knockback")
        .description("If to place the frame back")
        .defaultValue(false)
        .visible(Attributes::get)
        .build()
    );

    private final Setting<ItemGenerator.AttackKnockbackSlot> AttackKnockbackSlotType = sgAttributes.add(new EnumSetting.Builder<ItemGenerator.AttackKnockbackSlot>()
        .name("attack_knockback.slot")
        .defaultValue(AttackKnockbackSlot.AnySlot)
        .visible(AttackKnockback::get)
        .build()
    );

    private final Setting<Integer> AttackKnockbackAmount = sgAttributes.add(new IntSetting.Builder()
        .name("attack_knockback.amount")
        .description("asd")
        .sliderMin(0)
        .sliderMax(2147483647)
        .defaultValue(1)
        .visible(AttackKnockback::get)
        .build()
    );

    private final Setting<Boolean> AttackSpeed = sgAttributes.add(new BoolSetting.Builder()
        .name("generic.attack_speed")
        .description("If to place the frame back")
        .defaultValue(false)
        .visible(Attributes::get)
        .build()
    );

    private final Setting<ItemGenerator.AttackSpeedSlot> AttackSpeedSlotType = sgAttributes.add(new EnumSetting.Builder<ItemGenerator.AttackSpeedSlot>()
        .name("attack_speed.slot")
        .defaultValue(AttackSpeedSlot.AnySlot)
        .visible(AttackSpeed::get)
        .build()
    );

    private final Setting<Integer> AttackSpeedAmount = sgAttributes.add(new IntSetting.Builder()
        .name("attack_speed.amount")
        .description("asd")
        .sliderMin(0)
        .sliderMax(2147483647)
        .defaultValue(1)
        .visible(AttackSpeed::get)
        .build()
    );

    private final Setting<Boolean> FlyingSpeed = sgAttributes.add(new BoolSetting.Builder()
        .name("generic.flying_speed")
        .description("If to place the frame back")
        .defaultValue(false)
        .visible(Attributes::get)
        .build()
    );

    private final Setting<ItemGenerator.FlyingSpeedSlot> FlyingSpeedSlotType = sgAttributes.add(new EnumSetting.Builder<ItemGenerator.FlyingSpeedSlot>()
        .name("attack_damage.slot")
        .defaultValue(FlyingSpeedSlot.AnySlot)
        .visible(FlyingSpeed::get)
        .build()
    );

    private final Setting<Integer> FlyingSpeedAmount = sgAttributes.add(new IntSetting.Builder()
        .name("flyingSpeed.amount")
        .description("asd")
        .sliderMin(0)
        .sliderMax(2147483647)
        .defaultValue(1)
        .visible(FlyingSpeed::get)
        .build()
    );

    private final Setting<Boolean> HorseJumpStrength = sgAttributes.add(new BoolSetting.Builder()
        .name("horse.jump_strength")
        .description("If to place the frame back")
        .defaultValue(false)
        .visible(Attributes::get)
        .build()
    );

    private final Setting<ItemGenerator.JumpStrengthSlot> HorseJumpStrengthSlotType = sgAttributes.add(new EnumSetting.Builder<ItemGenerator.JumpStrengthSlot>()
        .name("horse.jump_strength.slot")
        .defaultValue(JumpStrengthSlot.AnySlot)
        .visible(HorseJumpStrength::get)
        .build()
    );

    private final Setting<Integer> HorseJumpStrengthAmount = sgAttributes.add(new IntSetting.Builder()
        .name("horse.jump_strength.amount")
        .description("asd")
        .sliderMin(0)
        .sliderMax(2147483647)
        .defaultValue(1)
        .visible(HorseJumpStrength::get)
        .build()
    );

    private final Setting<Boolean> KnockbackResistance = sgAttributes.add(new BoolSetting.Builder()
        .name("generic.knockback_resistance")
        .description("If to place the frame back")
        .defaultValue(false)
        .visible(Attributes::get)
        .build()
    );

    private final Setting<ItemGenerator.KnockbackresistanceSlot> KnockbackResistanceSlotType = sgAttributes.add(new EnumSetting.Builder<ItemGenerator.KnockbackresistanceSlot>()
        .name("knockback_resistance.slot")
        .defaultValue(KnockbackresistanceSlot.AnySlot)
        .visible(KnockbackResistance::get)
        .build()
    );

    private final Setting<Integer> KnockbackResistanceAmount = sgAttributes.add(new IntSetting.Builder()
        .name("knockback_resistance.amount")
        .description("asd")
        .sliderMin(0)
        .sliderMax(2147483647)
        .defaultValue(1)
        .visible(KnockbackResistance::get)
        .build()
    );

    private final Setting<Boolean> Luck = sgAttributes.add(new BoolSetting.Builder()
        .name("generic.luck")
        .description("If to place the frame back")
        .defaultValue(false)
        .visible(Attributes::get)
        .build()
    );

    private final Setting<ItemGenerator.LuckSlot> LuckSlotType = sgAttributes.add(new EnumSetting.Builder<ItemGenerator.LuckSlot>()
        .name("luck.slot")
        .defaultValue(LuckSlot.AnySlot)
        .visible(Luck::get)
        .build()
    );

    private final Setting<Integer> LuckAmount = sgAttributes.add(new IntSetting.Builder()
        .name("luck.amount")
        .description("asd")
        .sliderMin(0)
        .sliderMax(2147483647)
        .defaultValue(1)
        .visible(Luck::get)
        .build()
    );

    private final Setting<Boolean> MaxHealth = sgAttributes.add(new BoolSetting.Builder()
        .name("generic.max_health")
        .description("If to place the frame back")
        .defaultValue(false)
        .visible(Attributes::get)
        .build()
    );

    private final Setting<ItemGenerator.MaxHealthSlot> MaxHealthSlotType = sgAttributes.add(new EnumSetting.Builder<ItemGenerator.MaxHealthSlot>()
        .name("max_health.slot")
        .defaultValue(MaxHealthSlot.AnySlot)
        .visible(MaxHealth::get)
        .build()
    );

    private final Setting<Integer> MaxHealthAmount = sgAttributes.add(new IntSetting.Builder()
        .name("max_health.amount")
        .description("asd")
        .sliderMin(0)
        .sliderMax(2147483647)
        .defaultValue(1)
        .visible(MaxHealth::get)
        .build()
    );

    private final Setting<Boolean> FollowRange = sgAttributes.add(new BoolSetting.Builder()
        .name("generic.follow_range")
        .description("If to place the frame back")
        .defaultValue(false)
        .visible(Attributes::get)
        .build()
    );

    private final Setting<ItemGenerator.FollowRangeSlot> FollowRangeSlotType = sgAttributes.add(new EnumSetting.Builder<ItemGenerator.FollowRangeSlot>()
        .name("follow_range.slot")
        .defaultValue(FollowRangeSlot.AnySlot)
        .visible(FollowRange::get)
        .build()
    );

    private final Setting<Integer> FollowRangeAmount = sgAttributes.add(new IntSetting.Builder()
        .name("follow_range.amount")
        .description("asd")
        .sliderMin(0)
        .sliderMax(2147483647)
        .defaultValue(1)
        .visible(FollowRange::get)
        .build()
    );

    private final Setting<Boolean> Speed = sgAttributes.add(new BoolSetting.Builder()
        .name("generic.movement_speed")
        .description("If to place the frame back")
        .defaultValue(false)
        .visible(Attributes::get)
        .build()
    );

    private final Setting<ItemGenerator.MovementSpeedSlot> SpeedSlotType = sgAttributes.add(new EnumSetting.Builder<ItemGenerator.MovementSpeedSlot>()
        .name("movement_speed.slot")
        .defaultValue(MovementSpeedSlot.AnySlot)
        .visible(Speed::get)
        .build()
    );

    private final Setting<Integer> SpeedAmount = sgAttributes.add(new IntSetting.Builder()
        .name("movement_speed.amount")
        .description("asd")
        .sliderMin(0)
        .sliderMax(2147483647)
        .defaultValue(1)
        .visible(Speed::get)
        .build()
    );

    private final Setting<Boolean> ZombieReinforcements = sgAttributes.add(new BoolSetting.Builder()
        .name("zombie.spawn_reinforcements")
        .description("If to place the frame back")
        .defaultValue(false)
        .visible(Attributes::get)
        .build()
    );

    private final Setting<ItemGenerator.SpawnReinforcementsSlot> ZombieReinforcementsSlotType = sgAttributes.add(new EnumSetting.Builder<ItemGenerator.SpawnReinforcementsSlot>()
        .name("spawn_reinforcements.slot")
        .defaultValue(SpawnReinforcementsSlot.AnySlot)
        .visible(ZombieReinforcements::get)
        .build()
    );

    private final Setting<Integer> ZombieReinforcementsAmount = sgAttributes.add(new IntSetting.Builder()
        .name("spawn_reinforcements.amount")
        .description("asd")
        .sliderMin(0)
        .sliderMax(2147483647)
        .defaultValue(1)
        .visible(ZombieReinforcements::get)
        .build()
    );

    public enum NameColor {
        dark_red, red, gold, yellow, dark_green, green, aqua, dark_aqua, dark_blue, blue, light_purple, dark_purple, white, gray, dark_gray, black
    }

    public enum LoreColor {
        dark_red, red, gold, yellow, dark_green, green, aqua, dark_aqua, dark_blue, blue, light_purple, dark_purple, white, gray, dark_gray, black
    }

    public enum ArmorSlot {
        AnySlot, mainhand, offhand, head, chest, legs, feet
    }
    public enum ArmorToughnessSlot {
        AnySlot, mainhand, offhand, head, chest, legs, feet
    }
    public enum AttackDamageSlot {
        AnySlot, mainhand, offhand, head, chest, legs, feet
    }
    public enum AttackKnockbackSlot {
        AnySlot, mainhand, offhand, head, chest, legs, feet
    }
    public enum AttackSpeedSlot {
        AnySlot, mainhand, offhand, head, chest, legs, feet
    }
    public enum FlyingSpeedSlot {
        AnySlot, mainhand, offhand, head, chest, legs, feet
    }
    public enum JumpStrengthSlot {
        AnySlot, mainhand, offhand, head, chest, legs, feet
    }
    public enum KnockbackresistanceSlot {
        AnySlot, mainhand, offhand, head, chest, legs, feet
    }
    public enum LuckSlot {
        AnySlot, mainhand, offhand, head, chest, legs, feet
    }
    public enum MaxHealthSlot {
        AnySlot, mainhand, offhand, head, chest, legs, feet
    }
    public enum FollowRangeSlot {
        AnySlot, mainhand, offhand, head, chest, legs, feet
    }
    public enum MovementSpeedSlot {
        AnySlot, mainhand, offhand, head, chest, legs, feet
    }
    public enum SpawnReinforcementsSlot {
        AnySlot, mainhand, offhand, head, chest, legs, feet
    }

    private static final HashMap<Enchantment, String> enchantmentIds = new HashMap<>();
    static {
        enchantmentIds.put(Enchantments.SHARPNESS, "sharpness");
        enchantmentIds.put(Enchantments.PROTECTION, "protection");
        enchantmentIds.put(Enchantments.DEPTH_STRIDER, "depth_strider");
        enchantmentIds.put(Enchantments.FIRE_PROTECTION, "fire_protection");
        enchantmentIds.put(Enchantments.FEATHER_FALLING, "feather_falling");
        enchantmentIds.put(Enchantments.BLAST_PROTECTION, "blast_protection");
        enchantmentIds.put(Enchantments.PROJECTILE_PROTECTION, "projectile_protection");
        enchantmentIds.put(Enchantments.RESPIRATION, "respiration");
        enchantmentIds.put(Enchantments.AQUA_AFFINITY, "aqua_affinity");
        enchantmentIds.put(Enchantments.THORNS, "thorns");
        enchantmentIds.put(Enchantments.FROST_WALKER, "frost_walker");
        enchantmentIds.put(Enchantments.BINDING_CURSE, "binding_curse");
        enchantmentIds.put(Enchantments.SOUL_SPEED, "soul_speed");
        enchantmentIds.put(Enchantments.SWIFT_SNEAK, "swift_sneak");
        enchantmentIds.put(Enchantments.SMITE, "smite");
        enchantmentIds.put(Enchantments.BANE_OF_ARTHROPODS, "bane_of_arthropods");
        enchantmentIds.put(Enchantments.KNOCKBACK, "knockback");
        enchantmentIds.put(Enchantments.FIRE_ASPECT, "fire_aspect");
        enchantmentIds.put(Enchantments.LOOTING, "looting");
        enchantmentIds.put(Enchantments.SWEEPING, "sweeping");
        enchantmentIds.put(Enchantments.EFFICIENCY, "efficiency");
        enchantmentIds.put(Enchantments.SILK_TOUCH, "slik_touch");
        enchantmentIds.put(Enchantments.UNBREAKING, "unbreaking");
        enchantmentIds.put(Enchantments.FORTUNE, "fortune");
        enchantmentIds.put(Enchantments.POWER, "power");
        enchantmentIds.put(Enchantments.PUNCH, "punch");
        enchantmentIds.put(Enchantments.INFINITY, "infinity");
        enchantmentIds.put(Enchantments.LUCK_OF_THE_SEA, "luck_of_the_sea");
        enchantmentIds.put(Enchantments.LURE, "lure");
        enchantmentIds.put(Enchantments.LOYALTY, "loyality");
        enchantmentIds.put(Enchantments.IMPALING, "impaling");
        enchantmentIds.put(Enchantments.RIPTIDE, "ripdite");
        enchantmentIds.put(Enchantments.CHANNELING, "channeling");
        enchantmentIds.put(Enchantments.MULTISHOT, "multishot");
        enchantmentIds.put(Enchantments.QUICK_CHARGE, "quick_charge");
        enchantmentIds.put(Enchantments.PIERCING, "piercing");
        enchantmentIds.put(Enchantments.MENDING, "medning");
        enchantmentIds.put(Enchantments.VANISHING_CURSE, "vanishing_curse");
    }

    public void onActivate() {
        List<Item> itemList = item.get();

        if (itemList.isEmpty()) {
            info(" [ERROR] No items selected.");
            toggle();
            return;
        }

        if (itemList.size() > 1) {
            info(" [ERROR] More than one item selected. Please select only one item.");
            toggle();
            return;
        }

        StringBuilder enchantmentsTag = new StringBuilder();
        StringBuilder displayTag = new StringBuilder();
        StringBuilder attributesTag = new StringBuilder();


        if (Enchantment.get()) {
        for (Enchantment enchantment : EnchList.get()) {
            String enchantmentId = enchantmentIds.get(enchantment);
            if (enchantmentId != null) {
                int level = EnchInt.get();
                enchantmentsTag.append("{id:\"").append(enchantmentId).append("\",lvl:").append(level).append("},");
            }
        }}

        if (CustomName.get() && !CustomNameString.get().isEmpty()) {
            displayTag.append("Name:'[{\"text\":\"").append(CustomNameString.get()).append("\",\"italic\":").append(CustomNameItalic.get());
            if (CustomNameBold.get()) {
                displayTag.append(",\"bold\":true");
            }
            if (CustomNameUnderlined.get()) {
                displayTag.append(",\"underlined\":true");
            }
            if (CustomNameStriketrough.get()) {
                displayTag.append(",\"strikethrough\":true");
            }
            if (CustomNameColor.get()) {
                displayTag.append(",\"color\":\"" + CustomNameChoosenColor.get().toString() + "\"");
            }
            displayTag.append("}]'");
        }

        if (CustomLore.get() && !CustomLoreString.get().isEmpty()) {
            if (!displayTag.isEmpty()) {
                displayTag.append(",");
            }
            displayTag.append("Lore:['[{\"text\":\"").append(CustomLoreString.get()).append("\",\"italic\":").append(CustomLoreItalic.get());
            if (CustomLoreBold.get()) {
                displayTag.append(",\"bold\":true");
            }
            if (CustomLoreUnderlined.get()) {
                displayTag.append(",\"underlined\":true");
            }
            if (CustomLoreStriketrough.get()) {
                displayTag.append(",\"strikethrough\":true");
            }
            if (CustomLoreColor.get()) {
                displayTag.append(",\"color\":\"" + CustomLoreChoosenColor.get().toString() + "\"");
            }
            displayTag.append("}]']");
        }
        if (Extras.get()) {
            if (DyeColor.get() && !CustomNameString.get().isEmpty() && !CustomLoreString.get().isEmpty()) {
                if (!displayTag.isEmpty()) {
                    displayTag.append(",");
                }
                String rgbaString = DyeColorString.get().toString();
                String[] rgbValues = rgbaString.split(" ");
                if (rgbValues.length == 4) {
                    try {
                        int red = Integer.parseInt(rgbValues[0]);
                        int green = Integer.parseInt(rgbValues[1]);
                        int blue = Integer.parseInt(rgbValues[2]);
                        //int a = Integer.parseInt(rgbValues[3]);
                        String rgbtohex = String.format("%02X%02X%02X", red, green, blue);
                        int hextodecimal = Integer.parseInt(rgbtohex, 16);
                        displayTag.append("color:").append(hextodecimal);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid RGB format: " + rgbaString);
                    }
                } else {
                    System.out.println("Invalid RGB format: " + rgbaString);
                }
            }
        }
        if (Attributes.get()) {
            if (Armor.get()) {
                attributesTag.append("{AttributeName:\"generic.armor\",Amount:")
                    .append(ArmorAmount.get());
                if (!ArmorSlotType.get().equals(ItemGenerator.ArmorSlot.AnySlot)) {
                    attributesTag.append(",Slot:\"")
                        .append(ArmorSlotType.get().toString().toLowerCase())
                        .append("\"");
                }
                attributesTag.append(",Name:\"generic.armor\",UUID:[I;-" + ThreadLocalRandom.current().nextInt(10000,99999) + "," + ThreadLocalRandom.current().nextInt(10000,99999) + "," + ThreadLocalRandom.current().nextInt(10000,99999) + ",-" + ThreadLocalRandom.current().nextInt(10000,99999) +"]}");
                if (ArmorToughness.get() || AttackDamage.get() || AttackKnockback.get() || AttackSpeed.get() || FlyingSpeed.get() || HorseJumpStrength.get() || KnockbackResistance.get() || Luck.get() || MaxHealth.get() || FollowRange.get() || Speed.get() || ZombieReinforcements.get()) {
                    attributesTag.append(",");
                }
            }
            if (ArmorToughness.get()) {
                attributesTag.append("{AttributeName:\"generic.armor_toughness\",Amount:")
                    .append(ArmorToughnessAmount.get());
                if (!ArmorToughnessSlotType.get().equals(ItemGenerator.ArmorToughnessSlot.AnySlot)) {
                    attributesTag.append(",Slot:\"")
                        .append(ArmorToughnessSlotType.get().toString().toLowerCase())
                        .append("\"");
                }
                attributesTag.append(",Name:\"generic.attack_toughness\",UUID:[I;-" + ThreadLocalRandom.current().nextInt(10000,99999) + "," + ThreadLocalRandom.current().nextInt(10000,99999) + "," + ThreadLocalRandom.current().nextInt(10000,99999) + ",-" + ThreadLocalRandom.current().nextInt(10000,99999) +"]}");
                if (AttackDamage.get() || AttackKnockback.get() || AttackSpeed.get() || FlyingSpeed.get() || HorseJumpStrength.get() || KnockbackResistance.get() || Luck.get() || MaxHealth.get() || FollowRange.get() || Speed.get() || ZombieReinforcements.get()) {
                    attributesTag.append(",");
                }
            }
            if (AttackDamage.get()) {
                attributesTag.append("{AttributeName:\"generic.attack_damage\",Amount:")
                    .append(AttackDamageAmount.get());
                if (!AttackDamageSlotType.get().equals(ItemGenerator.AttackDamageSlot.AnySlot)) {
                    attributesTag.append(",Slot:\"")
                        .append(AttackDamageSlotType.get().toString().toLowerCase())
                        .append("\"");
                }
                attributesTag.append(",Name:\"generic.attack_damage\",UUID:[I;-" + ThreadLocalRandom.current().nextInt(10000,99999) + "," + ThreadLocalRandom.current().nextInt(10000,99999) + "," + ThreadLocalRandom.current().nextInt(10000,99999) + ",-" + ThreadLocalRandom.current().nextInt(10000,99999) +"]}");
                if (AttackKnockback.get() || AttackSpeed.get() || FlyingSpeed.get() || HorseJumpStrength.get() || KnockbackResistance.get() || Luck.get() || MaxHealth.get() || FollowRange.get() || Speed.get() || ZombieReinforcements.get()) {
                    attributesTag.append(",");
                }
            }
            if (AttackKnockback.get()) {
                attributesTag.append("{AttributeName:\"generic.attack_knockback\",Amount:")
                    .append(AttackKnockbackAmount.get());
                if (!AttackKnockbackSlotType.get().equals(ItemGenerator.AttackKnockbackSlot.AnySlot)) {
                    attributesTag.append(",Slot:\"")
                        .append(AttackKnockbackSlotType.get().toString().toLowerCase())
                        .append("\"");
                }
                attributesTag.append(",Name:\"generic.attack_knockback\",UUID:[I;-" + ThreadLocalRandom.current().nextInt(10000,99999) + "," + ThreadLocalRandom.current().nextInt(10000,99999) + "," + ThreadLocalRandom.current().nextInt(10000,99999) + ",-" + ThreadLocalRandom.current().nextInt(10000,99999) +"]}");
                if (AttackSpeed.get() || FlyingSpeed.get() || HorseJumpStrength.get() || KnockbackResistance.get() || Luck.get() || MaxHealth.get() || FollowRange.get() || Speed.get() || ZombieReinforcements.get()) {
                    attributesTag.append(",");
                }
            }
            if (AttackSpeed.get()) {
                attributesTag.append("{AttributeName:\"generic.attack_speed\",Amount:")
                    .append(AttackSpeedAmount.get());
                if (!AttackSpeedSlotType.get().equals(ItemGenerator.AttackSpeedSlot.AnySlot)) {
                    attributesTag.append(",Slot:\"")
                        .append(AttackSpeedSlotType.get().toString().toLowerCase())
                        .append("\"");
                }
                attributesTag.append(",Name:\"generic.attack_speed\",UUID:[I;-" + ThreadLocalRandom.current().nextInt(10000,99999) + "," + ThreadLocalRandom.current().nextInt(10000,99999) + "," + ThreadLocalRandom.current().nextInt(10000,99999) + ",-" + ThreadLocalRandom.current().nextInt(10000,99999) +"]}");
                if (FlyingSpeed.get() || HorseJumpStrength.get() || KnockbackResistance.get() || Luck.get() || MaxHealth.get() || FollowRange.get() || Speed.get() || ZombieReinforcements.get()) {
                    attributesTag.append(",");
                }
            }
            if (FlyingSpeed.get()) {
                attributesTag.append("{AttributeName:\"generic.flying_speed\",Amount:")
                    .append(FlyingSpeedAmount.get());
                if (!FlyingSpeedSlotType.get().equals(ItemGenerator.FlyingSpeedSlot.AnySlot)) {
                    attributesTag.append(",Slot:\"")
                        .append(FlyingSpeedSlotType.get().toString().toLowerCase())
                        .append("\"");
                }
                attributesTag.append(",Name:\"generic.flying_speed\",UUID:[I;-" + ThreadLocalRandom.current().nextInt(10000,99999) + "," + ThreadLocalRandom.current().nextInt(10000,99999) + "," + ThreadLocalRandom.current().nextInt(10000,99999) + ",-" + ThreadLocalRandom.current().nextInt(10000,99999) +"]}");
                if (HorseJumpStrength.get() || KnockbackResistance.get() || Luck.get() || MaxHealth.get() || FollowRange.get() || Speed.get() || ZombieReinforcements.get()) {
                    attributesTag.append(",");
                }
            }
            if (HorseJumpStrength.get()) {
                attributesTag.append("{AttributeName:\"horse.jump_strength\",Amount:")
                    .append(HorseJumpStrengthAmount.get());
                if (!HorseJumpStrengthSlotType.get().equals(ItemGenerator.JumpStrengthSlot.AnySlot)) {
                    attributesTag.append(",Slot:\"")
                        .append(HorseJumpStrengthSlotType.get().toString().toLowerCase())
                        .append("\"");
                }
                attributesTag.append(",Name:\"horse.jump_strength\",UUID:[I;-" + ThreadLocalRandom.current().nextInt(10000,99999) + "," + ThreadLocalRandom.current().nextInt(10000,99999) + "," + ThreadLocalRandom.current().nextInt(10000,99999) + ",-" + ThreadLocalRandom.current().nextInt(10000,99999) +"]}");
                if (KnockbackResistance.get() || Luck.get() || MaxHealth.get() || FollowRange.get() || Speed.get() || ZombieReinforcements.get()) {
                    attributesTag.append(",");
                }
            }
            if (KnockbackResistance.get()) {
                attributesTag.append("{AttributeName:\"generic.knockback_resistance\",Amount:")
                    .append(KnockbackResistanceAmount.get());
                if (!KnockbackResistanceSlotType.get().equals(ItemGenerator.KnockbackresistanceSlot.AnySlot)) {
                    attributesTag.append(",Slot:\"")
                        .append(KnockbackResistanceSlotType.get().toString().toLowerCase())
                        .append("\"");
                }
                attributesTag.append(",Name:\"generic.knockback_resistance\",UUID:[I;-" + ThreadLocalRandom.current().nextInt(10000,99999) + "," + ThreadLocalRandom.current().nextInt(10000,99999) + "," + ThreadLocalRandom.current().nextInt(10000,99999) + ",-" + ThreadLocalRandom.current().nextInt(10000,99999) +"]}");
                if (Luck.get() || MaxHealth.get() || FollowRange.get() || Speed.get() || ZombieReinforcements.get()) {
                    attributesTag.append(",");
                }
            }
            if (Luck.get()) {
                attributesTag.append("{AttributeName:\"generic.luck\",Amount:")
                    .append(LuckAmount.get());
                if (!LuckSlotType.get().equals(ItemGenerator.LuckSlot.AnySlot)) {
                    attributesTag.append(",Slot:\"")
                        .append(LuckSlotType.get().toString().toLowerCase())
                        .append("\"");
                }
                attributesTag.append(",Name:\"generic.luck\",UUID:[I;-" + ThreadLocalRandom.current().nextInt(10000,99999) + "," + ThreadLocalRandom.current().nextInt(10000,99999) + "," + ThreadLocalRandom.current().nextInt(10000,99999) + ",-" + ThreadLocalRandom.current().nextInt(10000,99999) +"]}");
                if (MaxHealth.get() || FollowRange.get() || Speed.get() || ZombieReinforcements.get()) {
                    attributesTag.append(",");
                }
            }
            if (MaxHealth.get()) {
                attributesTag.append("{AttributeName:\"generic.max_health\",Amount:")
                    .append(MaxHealthAmount.get());
                if (!MaxHealthSlotType.get().equals(ItemGenerator.MaxHealthSlot.AnySlot)) {
                    attributesTag.append(",Slot:\"")
                        .append(MaxHealthSlotType.get().toString().toLowerCase())
                        .append("\"");
                }
                attributesTag.append(",Name:\"generic.max_health\",UUID:[I;-" + ThreadLocalRandom.current().nextInt(10000,99999) + "," + ThreadLocalRandom.current().nextInt(10000,99999) + "," + ThreadLocalRandom.current().nextInt(10000,99999) + ",-" + ThreadLocalRandom.current().nextInt(10000,99999) +"]}");
                if (FollowRange.get() || Speed.get() || ZombieReinforcements.get()) {
                    attributesTag.append(",");
                }
            }
            if (FollowRange.get()) {
                attributesTag.append("{AttributeName:\"generic.follow_range\",Amount:")
                    .append(FollowRangeAmount.get());
                if (!FollowRangeSlotType.get().equals(ItemGenerator.FollowRangeSlot.AnySlot)) {
                    attributesTag.append(",Slot:\"")
                        .append(FollowRangeSlotType.get().toString().toLowerCase())
                        .append("\"");
                }
                attributesTag.append(",Name:\"generic.follow_range\",UUID:[I;-" + ThreadLocalRandom.current().nextInt(10000,99999) + "," + ThreadLocalRandom.current().nextInt(10000,99999) + "," + ThreadLocalRandom.current().nextInt(10000,99999) + ",-" + ThreadLocalRandom.current().nextInt(10000,99999) +"]}");
                if (Speed.get() || ZombieReinforcements.get()) {
                    attributesTag.append(",");
                }
            }
            if (Speed.get()) {
                attributesTag.append("{AttributeName:\"generic.movement_speed\",Amount:")
                    .append(SpeedAmount.get());
                if (!SpeedSlotType.get().equals(ItemGenerator.MovementSpeedSlot.AnySlot)) {
                    attributesTag.append(",Slot:\"")
                        .append(SpeedSlotType.get().toString().toLowerCase())
                        .append("\"");
                }
                attributesTag.append(",Name:\"generic.movement_speed\",UUID:[I;-" + ThreadLocalRandom.current().nextInt(10000,99999) + "," + ThreadLocalRandom.current().nextInt(10000,99999) + "," + ThreadLocalRandom.current().nextInt(10000,99999) + ",-" + ThreadLocalRandom.current().nextInt(10000,99999) +"]}");
                if (ZombieReinforcements.get()) {
                    attributesTag.append(",");
                }
            }
            if (ZombieReinforcements.get()) {
                attributesTag.append("{AttributeName:\"zombie.spawn_reinforcements\",Amount:")
                    .append(ZombieReinforcementsAmount.get());
                if (!ZombieReinforcementsSlotType.get().equals(ItemGenerator.SpawnReinforcementsSlot.AnySlot)) {
                    attributesTag.append(",Slot:\"")
                        .append(ZombieReinforcementsSlotType.get().toString().toLowerCase())
                        .append("\"");
                }
                attributesTag.append(",Name:\"zombie.spawn_reinforcements\",UUID:[I;-" + ThreadLocalRandom.current().nextInt(10000,99999) + "," + ThreadLocalRandom.current().nextInt(10000,99999) + "," + ThreadLocalRandom.current().nextInt(10000,99999) + ",-" + ThreadLocalRandom.current().nextInt(10000,99999) +"]}");
            }
        }

        Item selecteditem = itemList.get(0);
        String itemId = Registries.ITEM.getId(selecteditem).toString();
        String command = "/give @s " + itemId;
        if (!enchantmentsTag.isEmpty() || enchantmentsTag.isEmpty() || !displayTag.isEmpty() || !attributesTag.isEmpty() || Unbreakable.get() || CustomDamage.get() || CustomRepqairCost.get() || HideFlags.get() || Invisible.get()){
            command += "{";
            if (!enchantmentsTag.isEmpty()) {
                command += "Enchantments:[" + enchantmentsTag.substring(0, enchantmentsTag.length() - 1) + "]";
                if (!displayTag.isEmpty() || Unbreakable.get() || CustomDamage.get() || CustomRepqairCost.get()  || Invisible.get()) {
                    command += ",";
                }
            }
            if (enchantmentsTag.isEmpty()) {
                if (Enchantment.get()) {
                    command += "Enchantments:[{}]";
                    if (!displayTag.isEmpty() || Unbreakable.get() || CustomDamage.get() || CustomRepqairCost.get()  || Invisible.get()) {
                        command += ",";
                    }
                }
            }
            if (!attributesTag.isEmpty()) {
                command += "AttributeModifiers:[" + attributesTag + "]";
                if (!displayTag.isEmpty() || Unbreakable.get() || CustomDamage.get() || CustomRepqairCost.get()  || Invisible.get()) {
                    command += ",";
                }
            }
            if (!displayTag.isEmpty()) {
                command += "display:{" + displayTag + "}";
                if (Unbreakable.get() || CustomDamage.get() || CustomRepqairCost.get() || HideFlags.get()  || Invisible.get()) {
                    command += ",";
                }
            }
            if (Extras.get()) {
                if (Unbreakable.get()) {
                    command += "Unbreakable:1";
                    if (CustomDamage.get() || CustomRepqairCost.get() || HideFlags.get() || Invisible.get()) {
                        command += ",";
                    }
                }
                if (CustomDamage.get()) {
                    command += "Damage:" + Damage.get();
                    if (CustomRepqairCost.get() || HideFlags.get() || Invisible.get()) {
                        command += ",";
                    }
                }
                if (CustomRepqairCost.get()) {
                    command += "RepairCost:" + RepairCost.get();
                    if (HideFlags.get() || Invisible.get()) {
                        command += ",";
                    }
                }
                if (HideFlags.get()) {
                    command += "HideFlags:" + HideFlagsNum.get();
                    if (Invisible.get()) {
                        command += ",";
                    }
                }
                if (Invisible.get()) {
                    command += "EntityTag:{Invisible:1b}";
                }
            }
            command += "}";
        }
        ChatUtils.sendPlayerMsg(command + " " + ItemCount.get());
        toggle();
    }
}
