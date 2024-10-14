package Modelo;

public class BrickBreaker {
    private boolean play;
    private int score;
    private int vidas;
    private int totalBricks;
    private int playerX;
    private int ballposX;
    private int ballposY;
    private int ballXdir;
    private int ballYdir;
    private MapGenerator map;
    private LevelManager levelManager;
    private boolean ballLanzada; // Nueva variable para controlar si la pelota ha sido lanzada

    public BrickBreaker() {
        this.play = false;
        this.score = 0;
        this.vidas = 3;
        this.playerX = 310;
        this.ballposX = 310; // Posición inicial centrada sobre la barra
        this.ballposY = 550 - 20; // Posicionar justo encima de la barra
        this.ballXdir = 0;
        this.ballYdir = 0;
        this.ballLanzada = false;
        this.levelManager = new LevelManager();
        cargarNivelActual();
    }

    // Método para cargar el nivel actual
    public void cargarNivelActual() {
        String archivoNivel = levelManager.getNivelActual();
        if (archivoNivel != null) {
            this.map = new MapGenerator();
            map.cargarNivel(archivoNivel);
            this.totalBricks = map.getTotalBricks();
            reiniciarPelota();
            this.play = false;
        } else {
            setPlay(false);
        }
    }

    // Método para avanzar al siguiente nivel
    public void avanzarNivel() {
        if (levelManager.hayMasNiveles()) {
            levelManager.avanzarNivel();
            cargarNivelActual();
        } else {
            // Manejar caso donde no hay más niveles (e.g., mostrar mensaje de victoria final)
            setPlay(false);
        }
    }

    // Método para reiniciar la pelota
    public void reiniciarPelota() {
        this.ballposX = this.playerX + 40; // Centrar la pelota sobre la barra (100 de ancho de la barra / 2 - 10 de diámetro de la pelota)
        this.ballposY = 550 - 20; // Posicionar justo encima de la barra
        this.ballXdir = 0;
        this.ballYdir = 0;
        this.ballLanzada = false;
    }

    // Método para reiniciar el juego
    public void resetGame() {
        this.play = false;
        this.score = 0;
        this.vidas = 3;
        this.playerX = 310;
        cargarNivelActual();
        reiniciarPelota();
    }

    // Getters y Setters...
    public boolean isPlay() {
        return play;
    }

    public void setPlay(boolean play) {
        this.play = play;
    }

    public int getScore() {
        return score;
    }

    public int getVidas() { return vidas; }

    public void incrementScore(int value) {
        this.score += value;
    }
    public void decrementarVidas(){
        this.vidas --;
    }

    public int getTotalBricks() {
        return totalBricks;
    }

    public void decrementTotalBricks() {
        this.totalBricks--;
    }

    public int getPlayerX() {
        return playerX;
    }

    public void setPlayerX(int playerX) {
        this.playerX = playerX;
    }

    public int getBallposX() {
        return ballposX;
    }

    public void setBallposX(int ballposX) {
        this.ballposX = ballposX;
    }

    public int getBallposY() {
        return ballposY;
    }

    public void setBallposY(int ballposY) {
        this.ballposY = ballposY;
    }

    public int getBallXdir() {
        return ballXdir;
    }

    public void setBallXdir(int ballXdir) {
        this.ballXdir = ballXdir;
    }

    public int getBallYdir() {
        return ballYdir;
    }

    public void setBallYdir(int ballYdir) {
        this.ballYdir = ballYdir;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public boolean isBallLanzada() {
        return ballLanzada;
    }

    public void setBallLanzada(boolean ballLanzada) {
        this.ballLanzada = ballLanzada;
    }

    public MapGenerator getMap() {
        return map;
    }
}
