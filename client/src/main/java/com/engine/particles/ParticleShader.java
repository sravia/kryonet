package com.engine.particles;

import com.Config;
import com.engine.shaders.ShaderProgram;
import org.lwjgl.util.vector.Matrix4f;


public class ParticleShader extends ShaderProgram {

	private static final String VERTEX_FILE = Config.GAME_PATH+"particles/particleVShader.txt";
	private static final String FRAGMENT_FILE = Config.GAME_PATH+"particles/particleFShader.txt";

	private int location_projectionMatrix;
	private int location_numberOfRows;

	public ParticleShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_numberOfRows = super.getUniformLocation("numberOfRows");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "modelViewMatrix");
		super.bindAttribute(5, "texOffsets");
		super.bindAttribute(6, "blendFactor");
	}

	protected void loadNumberOfRows(float numberOfRows){
		super.loadFloat(location_numberOfRows,numberOfRows);
	}

	protected void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(location_projectionMatrix, projectionMatrix);
	}

}
