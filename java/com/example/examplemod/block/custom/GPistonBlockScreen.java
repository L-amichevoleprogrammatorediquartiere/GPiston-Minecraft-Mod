package com.example.examplemod.block.custom;

import com.example.examplemod.ExampleMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.core.Direction;

import org.jetbrains.annotations.NotNull;

public class GPistonBlockScreen extends Screen{
	private static final Component TITLE = Component.translatable("GPiston");
	
	private static final Component ABOVE = Component.translatable("Above");
	private static final Component BELOW = Component.translatable("Below");
	private static final Component LEFT = Component.translatable("Left");
	private static final Component RIGHT = Component.translatable("Right");
	private static final Component PLUS = Component.translatable("+");
	private static final Component LESS = Component.translatable("-");
	
	private static final ResourceLocation TEXTURE = new ResourceLocation(ExampleMod.MOD_ID,"textures/screen/gpiston_block_screen.png");
	
	private final BlockPos pPos;
	private final GPistonBlock pBlock;
	private final int imageWidth, imageHeight;
	
	private Direction playerLook,face;
	private Boolean isFirst;

	private int leftPos, topPos;
	private Button above,below,left,right,plus,less;
	
	public GPistonBlockScreen(BlockPos pPos,GPistonBlock pBlock,Direction playerLook, Direction face) {
		super(TITLE);
		
		this.pPos=pPos;
		this.imageWidth=176;
		this.imageHeight=166;
		this.pBlock=pBlock;
		this.playerLook=playerLook;
		this.face=face;
		isFirst=true;
	}
	
	protected void init() {
		super.init();
		
		this.leftPos = (this.width - this.imageWidth) / 2;
		this.topPos = (this.height - this.imageHeight) / 2;
		
		if(this.minecraft == null) return;
		Level level=this.minecraft.level;
		if(level == null) return;
		
		
		this.above = addRenderableWidget(
				Button.builder(
						ABOVE,
						this::handleAbove)
						.bounds(this.leftPos + 58,this.topPos + 36, 58, 18)
						.tooltip(Tooltip.create(ABOVE))
						.build());
		this.above = addRenderableWidget(
				Button.builder(
						BELOW,
						this::handleBelow)
						.bounds(this.leftPos + 58,this.topPos + 110, 58, 18)
						.tooltip(Tooltip.create(BELOW))
						.build());
		this.above = addRenderableWidget(
				Button.builder(
						LEFT,
						this::handleLeft)
						.bounds(this.leftPos + 0,this.topPos + 73, 58, 18)
						.tooltip(Tooltip.create(LEFT))
						.build());
		this.above = addRenderableWidget(
				Button.builder(
						RIGHT,
						this::handleRight)
						.bounds(this.leftPos + 116,this.topPos + 73, 58, 18)
						.tooltip(Tooltip.create(RIGHT))
						.build());
		this.above = addRenderableWidget(
				Button.builder(
						PLUS,
						this::handlePlus)
						.bounds(this.leftPos + 19,this.topPos + 128, 19, 18)
						.tooltip(Tooltip.create(PLUS))
						.build());
		this.above = addRenderableWidget(
				Button.builder(
						LESS,
						this::handleLess)
						.bounds(this.leftPos + 135,this.topPos + 128, 19, 18)
						.tooltip(Tooltip.create(LESS))
						.build());
	}
	
	private GuiGraphics pGraphics;
	private boolean isRendered=false;
	
	public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        //renderTransparentBackground(graphics);
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        super.render(graphics, mouseX, mouseY, partialTicks);
        
        this.pGraphics=graphics;
        this.isRendered=true;
        
        graphics.drawString(this.font,
                TITLE,
                this.leftPos + 8,
                this.topPos + 8,
                0x404040,
                false);

        graphics.drawString(this.font,
                "%d".formatted(pBlock.numBlock),
                this.leftPos + 70,
                this.topPos + 73,
                0xFF0000,
                false);
    }
	
	public boolean isPauseScreen() {
		return false;
	}
	
	private Directions fromDirtoDirs(Direction dir){
		if(dir==Direction.DOWN)
			return Directions.BELOW;
		else if(dir==Direction.EAST)
			return Directions.EAST;
		else if(dir==Direction.NORTH)
			return Directions.NORTH;
		else if(dir==Direction.SOUTH)
			return Directions.SOUTH;
		else if(dir==Direction.UP)
			return Directions.ABOVE;	
		else if(dir==Direction.WEST)
			return Directions.WEST;
		
		return null;
	}

	private Direction invertDirection(Direction dir){
		if(dir==Direction.DOWN)
			return Direction.UP;
		else if(dir==Direction.EAST)
			return Direction.WEST;
		else if(dir==Direction.NORTH)
			return Direction.SOUTH;
		else if(dir==Direction.SOUTH)
			return Direction.NORTH;
		else if(dir==Direction.UP)
			return Direction.DOWN;	
		else if(dir==Direction.WEST)
			return Direction.EAST;
		
		return null;
	}

	private Direction leftDirection(Direction dir){
		if(dir==Direction.EAST)
			return Direction.NORTH;
		else if(dir==Direction.NORTH)
			return Direction.WEST;
		else if(dir==Direction.SOUTH)
			return Direction.EAST;	
		else if(dir==Direction.WEST)
			return Direction.SOUTH;
		
		return null;
	}

	private void handleAbove(Button button) {
		//sopra se la facca è una di quelle laterali allora above è sempre up
		if(face==Direction.SOUTH || face==Direction.WEST ||
			face==Direction.NORTH || face==Direction.EAST){
				if(isFirst)
					pBlock.dirInput=Directions.ABOVE;
				else
					pBlock.dirOutput=Directions.ABOVE;
		}//qui se è una faccia laterale
		else if(face==Direction.UP){
			if(isFirst)
				pBlock.dirInput=fromDirtoDirs(playerLook);
			else
				pBlock.dirOutput=fromDirtoDirs(playerLook);
		}
		else{
			if(isFirst)
				pBlock.dirInput=fromDirtoDirs(invertDirection(playerLook));
			else
				pBlock.dirOutput=fromDirtoDirs(invertDirection(playerLook));
		}//below
		pBlock.updateEntity();
		isFirst=!isFirst;
	}
		
	private void handleBelow(Button button) {
		//sotto se la facca è una di quelle laterali allora below è sempre down
		if(face==Direction.SOUTH || face==Direction.WEST ||
			face==Direction.NORTH || face==Direction.EAST){
				if(isFirst)
					pBlock.dirInput=Directions.BELOW;
				else
					pBlock.dirOutput=Directions.BELOW;
		}//qui se è una faccia laterale
		else if(face==Direction.UP){
			if(isFirst)
				pBlock.dirInput=fromDirtoDirs(invertDirection(playerLook));
			else
				pBlock.dirOutput=fromDirtoDirs(invertDirection(playerLook));
		}
		else{
			if(isFirst)
				pBlock.dirInput=fromDirtoDirs(playerLook);
			else
				pBlock.dirOutput=fromDirtoDirs(playerLook);
		}//below
		pBlock.updateEntity();
		isFirst=!isFirst;
	}

	private void handleLeft(Button button) {
		if(isFirst)
			pBlock.dirInput=fromDirtoDirs(leftDirection(playerLook));
		else
			pBlock.dirOutput=fromDirtoDirs(leftDirection(playerLook));
		pBlock.updateEntity();
		isFirst=!isFirst;
	}

	private void handleRight(Button button) {
		if(isFirst)
			pBlock.dirInput=fromDirtoDirs(invertDirection(leftDirection(playerLook)));
		else
			pBlock.dirOutput=fromDirtoDirs(invertDirection(leftDirection(playerLook)));
		pBlock.updateEntity();
		isFirst=!isFirst;
	}

	private void handlePlus(Button button) {
		if(pBlock.numBlock<100) {
			pBlock.numBlock++;
			pBlock.updateEntity();
		}
	}

	private void handleLess(Button button) {
		if(pBlock.numBlock>1) {
			pBlock.numBlock--;
			pBlock.updateEntity();
		}
	}
}