package backstage.pgn;
/*
 * Copyright 2017 Ben-Hur Carlos Vieira Langoni Junior
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import backstage.game.Event;
import backstage.game.Game;
import backstage.game.GameResult;
import backstage.game.GenericPlayer;
import backstage.game.Player;
import backstage.game.PlayerType;
import backstage.game.Round;
import backstage.game.Termination;
import backstage.game.TimeControl;

import static backstage.pgn.PgnProperty.UTF8_BOM;
import static backstage.pgn.PgnProperty.isProperty;
import static backstage.pgn.PgnProperty.parsePgnProperty;

/**
 * A convenient loader to extract a chess game and its metadata from an iterator over the lines of the PGN file.
 * <p>
 * The implementation allows loading only a single PGN game at a time.
 */
public class GameLoader {

    /**
     * Loads the next game of chess from an iterator over the lines of a Portable Game Notation (PGN) file. The
     * iteration ends when the game is fully loaded, hence the iterator is not consumed more than necessary.
     *
     * @param iterator the iterator over the lines of a PGN file
     * @return the next game read from the iterator
     */
    public static Game loadNextGame(Iterator<String> iterator) {

        if (!iterator.hasNext()) {
            return null;
        }

        PgnTempContainer container = new PgnTempContainer();

        while (iterator.hasNext()) {
            String line = iterator.next().trim();
            if (line.startsWith(UTF8_BOM)) {
                line = line.substring(1);
            }
            try {
                if (isProperty(line)) {
                    addProperty(line, container);
                } else if (line.length() > 0) {
                    addMoveText(line, container);
                    if (isEndGame(line)) {
                        setMoveText(container.game, container.moveText);
                        return container.initGame ? container.game : null;
                    }
                }
            } catch (Exception e) {
                String name = container.event.getName();
                int r = container.round.getNumber();
                throw new PgnException(String.format("Error parsing PGN[%d, %s]: ", r, name), e);
            }
        }
        return container.initGame ? container.game : null;
    }

    private static void addProperty(String line, PgnTempContainer container) throws Exception {
        PgnProperty property = parsePgnProperty(line);
        if (property == null) {
            return;
        }
        container.initGame = true;
        String tag = property.name.toLowerCase().trim();
        //begin
        switch (tag) {
            case "event" -> {
                if (container.moveTextParsing && container.game.getHalfMoves().isEmpty()) {
                    setMoveText(container.game, container.moveText);
                }
                container.event.setName(property.value);
                container.event.setId(property.value);
            }
            case "site" -> container.event.setSite(property.value);
            case "date" -> container.event.setStartDate(property.value);
            case "round" -> {
                int r = 1;
                try {
                    r = Integer.parseInt(property.value);
                } catch (NumberFormatException e1) {
                }
                r = Math.max(0, r);
                container.round.setNumber(r);
                if (!container.event.getRound().containsKey(r)) {
                    container.event.getRound().put(r, container.round);
                }
            }
            case "white" ->  {
                if (container.round.getNumber() < 1) {
                    container.round.setNumber(1);
                }

                container.game.setDate(container.event.getStartDate());

                container.whitePlayer.setId(property.value);
                container.whitePlayer.setName(property.value);
                container.whitePlayer.setDescription(property.value);
            }
            case "black" ->  {
                if (container.round.getNumber() < 1) {
                    container.round.setNumber(1);
                }

                container.game.setDate(container.event.getStartDate());

                container.blackPlayer.setId(property.value);
                container.blackPlayer.setName(property.value);
                container.blackPlayer.setDescription(property.value);
            }
            case "result" -> container.game.setResult(GameResult.fromNotation(property.value));
            case "plycount" -> container.game.setPlyCount(property.value);
            case "termination" -> {
                try {
                    container.game.setTermination(Termination.fromValue(property.value.toUpperCase()));
                } catch (Exception e1) {
                    container.game.setTermination(Termination.UNTERMINATED);
                }
            }
            case "timecontrol" -> {
                if (container.event.getTimeControl() == null) {
                    try {
                        container.event.setTimeControl(TimeControl.parseFromString(property.value.toUpperCase()));
                    } catch (Exception e1) {
                        //ignore errors in time control tag as it's not required by standards
                    }
                }
            }
            case "annotator" -> container.game.setAnnotator(property.value);
            case "fen" -> container.game.setFen(property.value);
            case "eco" -> container.game.setEco(property.value);
            case "opening" -> container.game.setOpening(property.value);
            case "variation" -> container.game.setVariation(property.value);
            case "whiteelo" -> {
                try {
                    container.whitePlayer.setElo(Integer.parseInt(property.value));
                } catch (NumberFormatException e) {

                }
            }
            case "blackelo" -> {
                try {
                    container.blackPlayer.setElo(Integer.parseInt(property.value));
                } catch (NumberFormatException e) {

                }
            }
            default -> {
                if (container.game.getProperty() == null) {
                    container.game.setProperty(new HashMap<>());
                }
                container.game.getProperty().put(property.name, property.value);
            }
        }
    }

    private static void addMoveText(String line, PgnTempContainer container) {
        container.initGame = true;
        container.moveText.append(line);
        container.moveText.append('\n');
        container.moveTextParsing = true;
    }

    private static boolean isEndGame(String line) {
        return line.endsWith("1-0") || line.endsWith("0-1") || line.endsWith("1/2-1/2") || line.endsWith("*");
    }

    private static class PgnTempContainer {

        final Event event;
        final Round round;
        final Game game;
        Player whitePlayer;
        Player blackPlayer;
        final StringBuilder moveText;
        boolean moveTextParsing;
        boolean initGame;

        PgnTempContainer() {
            this.event = new Event();
            this.round = new Round(event);
            this.game = new Game(UUID.randomUUID().toString(), round);
            this.round.getGame().add(this.game);
            this.whitePlayer = new GenericPlayer();
            this.whitePlayer.setType(PlayerType.HUMAN);
            this.game.setWhitePlayer(this.whitePlayer);
            this.blackPlayer = new GenericPlayer();
            this.blackPlayer.setType(PlayerType.HUMAN);
            this.game.setBlackPlayer(this.blackPlayer);
            this.moveText = new StringBuilder();
        }
    }

    private static void setMoveText(Game game, StringBuilder moveText) throws Exception {

        //clear game result
        moveText = new StringBuilder(moveText.toString().replaceAll("1-0", ""));
        moveText = new StringBuilder(moveText.toString().replaceAll("0-1", ""));
        moveText = new StringBuilder(moveText.toString().replaceAll("1/2-1/2", ""));
        moveText = new StringBuilder(moveText.toString().replaceAll("\\*", ""));

        game.setMoveText(moveText);
        game.loadMoveText(moveText);

        game.setPlyCount(String.valueOf(game.getHalfMoves().size()));

    }
}