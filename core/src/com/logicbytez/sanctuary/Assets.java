package com.logicbytez.sanctuary;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;

public final class Assets{
	public static Array<TiledMap> hallways;
	public static BitmapFont font25, font50, fontHud;
	public static Music music_Exploration;
	public static ShaderProgram vignette;
	public static Sound sound_Door, sound_EidolonHurt, sound_InsertSunstone, sound_PlayerHurt, sound_SwordSlash, sound_SwordSwing;
	private static TextureAtlas atlas;
	public static TextureRegion texture_Altar, texture_Column, texture_Door, texture_Eidolon, texture_Gravestone, texture_HealthIndicator;
	public static TextureRegion texture_HudCrystal, texture_HudSunstone;
	public static TextureRegion texture_PadButton, texture_PadDiagonal, texture_PadOutline, texture_PadStraight;
	public static TextureRegion texture_PauseBar, texture_PedestalCrystal, texture_PedestalStone, texture_PlayerBlue, texture_PlayerRed, texture_Portal;
	public static TiledMap room_Antechamber_Up, room_Antechamber_Down, room_Antechamber_Left, room_Antechamber_Right, room_Sanctuary;
	public static TiledMap room_Pedestal_Up, room_Pedestal_Down, room_Pedestal_Left, room_Pedestal_Right;
	
	//never instantiate this class
	private Assets(){}

	//loads all of the assets into memory
	public static void load(){
		atlas = new TextureAtlas("atlas/atlas.pac");
		music_Exploration = Gdx.audio.newMusic(Gdx.files.internal("music/exploration.mp3"));
		music_Exploration.setLooping(true);
		music_Exploration.setVolume(.25f);
		ShaderProgram.pedantic = false;
		vignette = new ShaderProgram(Gdx.files.internal("shader/vignette.vsh"), Gdx.files.internal("shader/vignette.fsh"));
		findTextureRegions();
		generateFonts();
		loadRooms();
		loadSounds();
	}

	//locates all of the texture regions within the atlas
	private static void findTextureRegions(){
		texture_Altar = atlas.findRegion("altar");
		texture_Column = atlas.findRegion("column");
		texture_Door = atlas.findRegion("door");
		texture_Eidolon = atlas.findRegion("eidolon");
		texture_Gravestone = atlas.findRegion("gravestone");
		texture_HealthIndicator = atlas.findRegion("health_indicator");
		texture_HudCrystal = atlas.findRegion("hud_crystal");
		texture_HudSunstone = atlas.findRegion("hud_sunstone");
		texture_PadButton = atlas.findRegion("pad_button");
		texture_PadDiagonal = atlas.findRegion("pad_diagonal");
		texture_PadOutline = atlas.findRegion("pad_outline");
		texture_PadStraight = atlas.findRegion("pad_straight");
		texture_PauseBar = atlas.findRegion("pause_bar");
		texture_PedestalCrystal = atlas.findRegion("pedestal_crystal");
		texture_PedestalStone = atlas.findRegion("pedestal_stone");
		texture_PlayerBlue = atlas.findRegion("player_blue");
		texture_PlayerRed = atlas.findRegion("player_red");
		texture_Portal = atlas.findRegion("portal");
	}

	//creates all of the fonts
	private static void generateFonts(){
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.borderColor = Color.DARK_GRAY;
		parameter.borderWidth = 2;
		parameter.size = 25;
		font25 = generator.generateFont(parameter);
		parameter.size = 50;
		font50 = generator.generateFont(parameter);
		parameter.size = 25;
		fontHud = generator.generateFont(parameter);
		generator.dispose();
	}

	//loads all of the possible rooms
	private static void loadRooms(){
		hallways = new Array<TiledMap>();
		hallways.add(null);
		for(int i = 1; i < 16; i++){
			hallways.add(new TmxMapLoader().load("maps/hallways/hallway_" + i + ".tmx"));
		}
		room_Antechamber_Up = new TmxMapLoader().load("maps/antechamber/room_antechamber_up.tmx");
		room_Antechamber_Down = new TmxMapLoader().load("maps/antechamber/room_antechamber_down.tmx");
		room_Antechamber_Left = new TmxMapLoader().load("maps/antechamber/room_antechamber_left.tmx");
		room_Antechamber_Right = new TmxMapLoader().load("maps/antechamber/room_antechamber_right.tmx");
		room_Pedestal_Up = new TmxMapLoader().load("maps/pedestals/pedestal_up.tmx");
		room_Pedestal_Down = new TmxMapLoader().load("maps/pedestals/pedestal_down.tmx");
		room_Pedestal_Left = new TmxMapLoader().load("maps/pedestals/pedestal_left.tmx");
		room_Pedestal_Right = new TmxMapLoader().load("maps/pedestals/pedestal_right.tmx");
		room_Sanctuary = new TmxMapLoader().load("maps/room_sanctuary.tmx");
	}
	
	//loads all of the sounds
	private static void loadSounds(){
		sound_Door = Gdx.audio.newSound(Gdx.files.internal("sounds/door.wav"));
		sound_EidolonHurt = Gdx.audio.newSound(Gdx.files.internal("sounds/eidolon_hurt.wav"));
		sound_InsertSunstone = Gdx.audio.newSound(Gdx.files.internal("sounds/insert_sunstone.wav"));
		sound_PlayerHurt = Gdx.audio.newSound(Gdx.files.internal("sounds/player_hurt.wav"));
		sound_SwordSlash = Gdx.audio.newSound(Gdx.files.internal("sounds/sword_slash.wav"));
		sound_SwordSwing = Gdx.audio.newSound(Gdx.files.internal("sounds/sword_swing.wav"));
	}

	//creates an animation for a being
	public static Animation<TextureRegion> animate(int columns, int rows, int row, TextureRegion texture){
		TextureRegion[][] array = texture.split(texture.getRegionWidth() / columns, texture.getRegionHeight() / rows);
		TextureRegion[] frames = new TextureRegion[columns];
		int index = 0;
		for(int x = 0; x < columns; x++){
			frames[index++] = array[row][x];
		}
		return new Animation<TextureRegion>(1, frames);
	}

	//frees memory that was stored
	static void dispose(){
		atlas.dispose();
		font25.dispose();
		font50.dispose();
		music_Exploration.dispose();
		room_Antechamber_Up.dispose();
		room_Antechamber_Down.dispose();
		room_Antechamber_Left.dispose();
		room_Antechamber_Right.dispose();
		room_Pedestal_Up.dispose();
		room_Pedestal_Down.dispose();
		room_Pedestal_Left.dispose();
		room_Pedestal_Right.dispose();
		room_Sanctuary.dispose();
		sound_Door.dispose();
		sound_EidolonHurt.dispose();
		sound_InsertSunstone.dispose();
		sound_PlayerHurt.dispose();
		sound_SwordSlash.dispose();
		sound_SwordSwing.dispose();
		vignette.dispose();
		for(int i = 1; i < 16; i++){
			hallways.get(i).dispose();
		}
	}
}