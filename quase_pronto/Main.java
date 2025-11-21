import control.Controller;
import model.MusicPlayer;
import view.UserInterface;

void main() throws Exception {
    Controller controller = new Controller();
    MusicPlayer player = new MusicPlayer();
    UserInterface screen = new UserInterface();

    controller.setScreen(screen);
    controller.setPlayer(player);
    screen.setController(controller);
}
