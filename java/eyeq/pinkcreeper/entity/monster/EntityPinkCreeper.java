package eyeq.pinkcreeper.entity.monster;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class EntityPinkCreeper extends EntityCreeper {
    public EntityPinkCreeper(World world) {
        super(world);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();

        List<EntityAITasks.EntityAITaskEntry> removeList = new ArrayList<>();
        int i = 0;
        for(EntityAITasks.EntityAITaskEntry taskEntry : this.targetTasks.taskEntries) {
            if(i == 0) {
                removeList.add(taskEntry);
            }
            i++;
        }
        for(EntityAITasks.EntityAITaskEntry taskEntry : removeList) {
            this.targetTasks.taskEntries.remove(taskEntry);
        }
        this.targetTasks.addTask(1, new EntityAIFindPlayer(this, EntityPlayer.class, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0);
    }

    @Override
    protected Item getDropItem() {
        return Items.SUGAR;
    }

    @Override
    protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
        super.dropLoot(wasRecentlyHit, lootingModifier, source);
        this.dropFewItems(wasRecentlyHit, lootingModifier);
    }

    private boolean shouldAttackPlayer(EntityPlayer player) {
        if(player == null) {
            return false;
        }
        ItemStack itemStack = player.inventory.armorItemInSlot(EntityEquipmentSlot.HEAD.getIndex());

        if(itemStack.getItem() == Item.getItemFromBlock(Blocks.PUMPKIN)) {
            return false;
        }
        Vec3d playerLook = player.getLook(1.0F).normalize();
        Vec3d diff = new Vec3d(this.posX - player.posX, this.getEntityBoundingBox().minY + this.getEyeHeight() - (player.posY + player.getEyeHeight()), this.posZ - player.posZ);
        double d0 = diff.lengthVector();
        diff = diff.normalize();
        double d1 = playerLook.dotProduct(diff);
        return d1 > 1.0 - 0.025 / d0 && player.canEntityBeSeen(this);
    }

    public class EntityAIFindPlayer extends EntityAINearestAttackableTarget {
        private final EntityPinkCreeper entity;

        private EntityPlayer player;
        private int tick;

        public EntityAIFindPlayer(EntityPinkCreeper entity, Class targetClass, boolean targetChange) {
            super(entity, targetClass, targetChange);
            this.entity = entity;
        }

        @Override
        public boolean shouldExecute() {
            double d0 = this.getTargetDistance();
            player = entity.world.getNearestAttackablePlayer(entity.posX, entity.posY, entity.posZ, d0, d0, null, entity::shouldAttackPlayer);
            return player != null;
        }

        @Override
        public void startExecuting() {
            tick = 5;
        }

        @Override
        public void resetTask() {
            player = null;
            entity.setCreeperState(-1);
            super.resetTask();
        }

        @Override
        public boolean continueExecuting() {
            if(this.player != null) {
                if(!this.entity.shouldAttackPlayer(this.player)) {
                    return false;
                }
                this.entity.faceEntity(this.player, 10.0F, 10.0F);
                return true;
            }
            return this.targetEntity != null && this.targetEntity.isEntityAlive() || super.continueExecuting();
        }

        @Override
        public void updateTask() {
            if(player == null) {
                if(this.targetEntity != null) {
                    entity.setCreeperState(1);
                }
                super.updateTask();
                return;
            }
            tick--;
            if(tick <= 0) {
                targetEntity = player;
                player = null;
                super.startExecuting();
            }
        }
    }
}
