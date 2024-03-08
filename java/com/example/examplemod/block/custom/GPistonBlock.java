package com.example.examplemod.block.custom;

import com.example.examplemod.block.ModBlocks;
import com.example.examplemod.block.custom.GPistonBlockScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.ticks.LevelTicks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.fml.DistExecutor;

import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings("unused")
public class GPistonBlock extends Block implements EntityBlock{
	
	private GPistonBlockEntity gpiston;
	public int numBlock;
    public Directions dirInput,dirOutput;
	
	public GPistonBlock(Properties pProperties) {
        super(pProperties);
        //metodo che imposta di default le posizioni dei blocchi
        //Viene richiamato solo quando vengono modificati i seguenti parametri:
        //dirInput or dirOutput or numBlock
    }

	public void updateEntity(){
		gpiston.setNumBlock(numBlock);
		gpiston.setDirInput(dirInput);
		gpiston.setDirOutput(dirOutput);
	}

    @Override  
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		if(pHand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;
    	if(!pLevel.isClientSide()){
			//server
			GPistonBlockEntity be=(GPistonBlockEntity)pLevel.getBlockEntity(pPos);
			gpiston=be;
			numBlock=be.getNumBlock();
			dirInput=be.getDirInput();
			dirOutput=be.getDirOutput();
			return InteractionResult.SUCCESS;
		}
		//client 
		Vec3 lookVec= pPlayer.getLookAngle();
		double x= lookVec.x();
		double z= lookVec.z();
		Direction playerLook;
		if (Math.abs(x) > Math.abs(z)) {
            if (x > 0) {
                // Guarda verso est
				playerLook=Direction.EAST;
            } else {
                // Guarda verso ovest
				playerLook=Direction.WEST;
            }
        } else {
            if (z > 0) {
                // Guarda verso sud
				playerLook=Direction.SOUTH;
            } else {
                // Guarda verso nord
				playerLook=Direction.NORTH;
            }
        }
		Direction face=pHit.getDirection();
		Minecraft.getInstance().setScreen(new GPistonBlockScreen(pPos,this,playerLook,face));
        return InteractionResult.SUCCESS;
    }
	
	public void neighborChanged(BlockState p_55666_, Level p_55667_, BlockPos p_55668_, Block p_55669_, BlockPos p_55670_, boolean p_55671_) {
		if (!p_55667_.isClientSide()) {
			boolean isNeighborPowered=p_55667_.hasNeighborSignal(p_55668_);
			
			int x=p_55668_.getX(),y=p_55668_.getY(),z=p_55668_.getZ();
			BlockPos gPos=new BlockPos(x, y, z);

			GPistonBlockEntity be=(GPistonBlockEntity)p_55667_.getBlockEntity(gPos);
			
			if(!be.getIsPowered() && isNeighborPowered) {
				//System.out.println("Sono stato alimentato");
				//Muovo i blocchi qui!!!!!
				if(GPistonBlockEntity.blockNearMe(gPos, p_55667_,false)==null){//non deve stare un mio blocco che sta attivo vicino a me
					System.out.println("acceso da :" + p_55670_);
					be.changeState(true, p_55667_,p_55668_,true);
					p_55667_.playSound(null, gPos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0f, 1.0f);
				}
				
			}
			else if (be.getIsPowered() && !isNeighborPowered && p_55670_.getY()+1!=gPos.getY() && p_55670_.getY()-1!=gPos.getY()){
				//System.out.println("Sono stato spento");
				//Rimetto a posto i blocchi qui!!!!
				//if(GPistonBlockEntity.blockNearMe(gPos, p_55667_,false)==null){
					System.out.println("spento da :" + p_55670_);
					be.changeState(false, p_55667_,p_55668_,false);
					p_55667_.playSound(null, gPos, SoundEvents.PISTON_CONTRACT, SoundSource.BLOCKS, 1.0f, 1.0f);
				//}
			}
	    }
	}

	public BlockEntity newBlockEntity(BlockPos pos,BlockState state){
		return ModBlocks.GPISTON_BLOCK_ENTITY.get().create(pos,state);
	}
}