package com.example.examplemod.block.custom;

import com.example.examplemod.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.Collections;

public class GPistonBlockEntity extends BlockEntity{
    
    private boolean isPowered=false;
    private int numBlock=4;

    private Directions dirInput=Directions.BELOW;
    private Directions dirOutput=Directions.ABOVE;

    private ArrayList<BlockPos> posBlocks= new ArrayList<BlockPos>(Collections.nCopies(numBlock*2, null));

	public boolean starting;

    public GPistonBlockEntity(BlockPos pos,BlockState state){
        super(ModBlocks.GPISTON_BLOCK_ENTITY.get(),pos, state);
    }

	public void setDirInput(Directions dir){
		dirInput=dir;
	}

	public Directions getDirInput(){
		return dirInput;
	}

	public Directions getDirOutput(){
		return dirOutput;
	}

	public void setDirOutput(Directions dir){
		dirOutput=dir;
	}

    public boolean getIsPowered(){
        return isPowered;
    }

    public void setIsPowered(boolean isPowered){
        this.isPowered=isPowered;
    }

    public void setNumBlock(int num) {
		if(num>numBlock){
			posBlocks.ensureCapacity(num*2);
			for(int i=(numBlock*2)-1;i<num*2;i++){
				posBlocks.add(null);
			}
		}
		numBlock=num;
	}
	
	public int getNumBlock() {
		return this.numBlock;
	}



    //funzione che assegna coordinate all'ArrayList in base al numero di blocchi e le posizioni
    private void setArrayPos(BlockPos posBlock) {
        //System.out.println(numBlock);
		for(int b=0;b<numBlock*2;b++) {
			if(b<numBlock) {
				switch (dirInput) {
				case ABOVE: //Y+
					posBlocks.set(b, posBlock.above(numBlock-b));
					break;
				case BELOW: //Y-
					posBlocks.set(b, posBlock.below(numBlock-b));
					break;
				case NORTH: //Z-
					posBlocks.set(b, posBlock.north(numBlock-b));
					break;
				case SOUTH: //Z+
					posBlocks.set(b, posBlock.south(numBlock-b));
					break;
				case WEST:  //X-
					posBlocks.set(b, posBlock.west(numBlock-b));
					break;
				case EAST:   //X+
					posBlocks.set(b, posBlock.east(numBlock-b));
					break;
				}
			}
			else {
				switch (dirOutput) {
				case ABOVE: //Y+
					posBlocks.set(b, posBlock.above(b-numBlock+1));
					break;
				case BELOW: //Y-
					posBlocks.set(b, posBlock.below(b-numBlock+1));
					break;
				case NORTH: //Z-
					posBlocks.set(b, posBlock.north(b-numBlock+1));
					break;
				case SOUTH: //Z+
					posBlocks.set(b, posBlock.south(b-numBlock+1));
					break;
				case WEST:  //X-
					posBlocks.set(b, posBlock.west(b-numBlock+1));
					break;
				case EAST:   //X+
					posBlocks.set(b, posBlock.east(b-numBlock+1));
					break;
				}
			}
		}
	}

	public static BlockPos blockNearMe(BlockPos pos,Level level,boolean isPowered){
		if(level.getBlockState(pos.above()).getBlock() instanceof GPistonBlock){
			GPistonBlockEntity be=(GPistonBlockEntity)level.getBlockEntity(pos.above());
			if(be.getIsPowered() != isPowered){
				return pos.above();
			}
		}
		if(level.getBlockState(pos.below()).getBlock() instanceof GPistonBlock){
			GPistonBlockEntity be=(GPistonBlockEntity)level.getBlockEntity(pos.below());
			if(be.getIsPowered() != isPowered){
				return pos.below();
			}
		}
		if(level.getBlockState(pos.north()).getBlock() instanceof GPistonBlock){
			GPistonBlockEntity be=(GPistonBlockEntity)level.getBlockEntity(pos.north());
			if(be.getIsPowered() != isPowered){
				return pos.north();
			}
		}
		if(level.getBlockState(pos.south()).getBlock() instanceof GPistonBlock){
			GPistonBlockEntity be=(GPistonBlockEntity)level.getBlockEntity(pos.south());
			if(be.getIsPowered() != isPowered){
				return pos.south();
			}
		}
		if(level.getBlockState(pos.west()).getBlock() instanceof GPistonBlock){
			GPistonBlockEntity be=(GPistonBlockEntity)level.getBlockEntity(pos.west());
			if(be.getIsPowered() != isPowered){
				return pos.west();
			}
		}
		if(level.getBlockState(pos.east()).getBlock() instanceof GPistonBlock){
			GPistonBlockEntity be=(GPistonBlockEntity)level.getBlockEntity(pos.east());
			if(be.getIsPowered() != isPowered){
				return pos.east();
			}
		}
		System.out.println("non ho trovato più nessuno...");
		return null;
	}


	public void changeState(boolean isPowered, Level plevel,BlockPos posBlock,boolean inOrOut){
		this.setIsPowered(isPowered);
		System.out.println(posBlock + "  " +  this.getIsPowered());
		this.moveBlock(plevel, posBlock, inOrOut);
		//here check if block near me are istance of GPistonBlock if yes call changeState again for this block
		BlockPos nextBlock=blockNearMe(posBlock,plevel,isPowered);
		if(nextBlock!=null){
			GPistonBlockEntity be=(GPistonBlockEntity)plevel.getBlockEntity(nextBlock);
			be.changeState(isPowered,plevel,nextBlock,inOrOut);
		}
	}

    //funzione che muove i blocchi (se inOrOut= true allora escono se =false allora rientrano)
    private void moveBlock(Level plevel,BlockPos posBlock,boolean inOrOut) {	//in true, out false
		setArrayPos(posBlock);
		//System.out.println(posBlock);
		for(int b=0;b<numBlock;b++) {
			if(inOrOut) {//Esce
				for(int j=numBlock*2-1; j>0; j--) {
					BlockState BlockToMove=plevel.getBlockState(posBlocks.get(j-1));
					plevel.removeBlock(posBlocks.get(j-1), false);
					plevel.setBlockAndUpdate(posBlocks.get(j), BlockToMove);
				}
			}
			else {       //Rientra
				for(int j=0; j<numBlock*2-1; j++) {
					BlockState BlockToMove=plevel.getBlockState(posBlocks.get(j+1));
					plevel.removeBlock(posBlocks.get(j+1), false);
					plevel.setBlockAndUpdate(posBlocks.get(j), BlockToMove);
				}
			}
		}
	}

	@Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putBoolean("isPowered", this.isPowered);
        compound.putInt("numBlock", this.numBlock);
        compound.putInt("dirInput", this.dirInput.ordinal());
        compound.putInt("dirOutput", this.dirOutput.ordinal());

        // Salva gli elementi dell'ArrayList come array di BlockPos
        ListTag posBlocksTag = new ListTag();
        for (BlockPos pos : this.posBlocks) {
            if (pos != null) {
                CompoundTag posTag = new CompoundTag();
                posTag.putInt("x", pos.getX());
                posTag.putInt("y", pos.getY());
                posTag.putInt("z", pos.getZ());
                posBlocksTag.add(posTag);
            }
        }
        compound.put("posBlocks", posBlocksTag);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.isPowered = compound.getBoolean("isPowered");
        this.numBlock = compound.getInt("numBlock");
        this.dirInput = Directions.values()[compound.getInt("dirInput")];
        this.dirOutput = Directions.values()[compound.getInt("dirOutput")];

        // Carica gli elementi dell'ArrayList
        ListTag posBlocksTag = compound.getList("posBlocks", 10); // Tipo 10 è TAG_Compound
        posBlocks.ensureCapacity(posBlocksTag.size());
		for (int i = 0; i < posBlocksTag.size(); i++) {
            CompoundTag posTag = posBlocksTag.getCompound(i);
            int x = posTag.getInt("x");
            int y = posTag.getInt("y");
            int z = posTag.getInt("z");
			if(i<7)
				this.posBlocks.set(i, new BlockPos(x, y, z));
			else
				this.posBlocks.add(i, new BlockPos(x, y, z));
        }
    }

}
