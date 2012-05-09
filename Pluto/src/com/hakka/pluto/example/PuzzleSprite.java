package com.hakka.pluto.example;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.hakka.pluto.example.module.Puzzle;
import com.hakka.pluto.example.module.Puzzle2;

public class PuzzleSprite extends Sprite{

	private Puzzle2 puzzle;

	public PuzzleSprite(int pX, int pY, ITextureRegion iTextureRegion,
			VertexBufferObjectManager vertexBufferObjectManager) {
		super(pX,pY,iTextureRegion,vertexBufferObjectManager);
	}

	public void setPuzzle(Puzzle2 pCard) {
		this.puzzle = pCard;
	}
	
	public Puzzle2 getPuzzle(){
		return puzzle;
	}

	public boolean isCollide() {
		return puzzle.getAnswer().collidesWith(this);
	}

	public void bgClick() {
		puzzle.getAnswer().setColor(1, 0 , 0);
	}

	public void bgUnClick() {
		puzzle.getAnswer().setColor(0, 1 , 0);
	}

}
