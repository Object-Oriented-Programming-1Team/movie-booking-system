package moviebooking.service;

import moviebooking.common.Manageable;
import moviebooking.model.Movie;
import moviebooking.model.Screen;
import moviebooking.model.Screening;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ScreenManager extends BaseManager<Screen, String> {

    @Override
    public Screen findById(String s) {
        for (Screen screen : list) {
            if (screen.getScreenId().equals(s)) {
                return screen;
            }
        }
        return null;
    }

    @Override
    protected Screen readItem(Scanner scan) {
        try{
            Screen screen=new Screen();
            screen.setScreenId(scan.next());
            String nameWithUnderBar=scan.next();
            screen.setScreenType(scan.next());
            screen.setDefaultPrice(scan.nextInt());
            screen.setRows(scan.nextInt());
            screen.setCols(scan.nextInt());

            screen.setScreenName(nameWithUnderBar.replace("_", " "));
            return screen;

        } catch (InputMismatchException e){
            System.out.println("[ERROR] ScreenManager: InputMismatch " + e.getMessage());
            scan.nextLine();
            return null;
        } catch (NoSuchElementException e) {
            //파일 끝에 도달했거나 형식이 맞지 않음
            return null;
        }
    }
}
