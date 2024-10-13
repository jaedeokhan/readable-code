# Readable Code

## Section 2-4 이름 짓기
MinesweeperGame.java에서 기존에 존재하는 의미가 명확하지 않은 변수들 리펙토링

## Section 2-6 메서드 선언부
* 메서드 선언부는 당연하게 `반환타입 메서드명 ( 파라미터 ) {}` 로 구성
* MinesweeperGame에서 사용하고 있는 코드들을 메서드로 뽑아보기
* showGameStartComments()
* initailzedGame()
* convertColFrom(cellInputCol)
  * convertColFrom 처럼 마지막에 From이라는 전치사를 붙이면 뒤의
    파라미터와 의미를 명확하게 할 수 있다.
* converRowFrom(cellInputRow)
  * converRowFrom의 구현부는 1줄이지만 추상화를 하는게 좋다.
    * `코드의 양이 중요한게 아니라, 코드의 의미가 중요하다.`
* checkIfGameIsClear()
  * 해당 메서드에서는 역할이 두 개가 있어서 1번은 isAllCellOpened()
    메서드로 한 번더 나눠준다.
    * 1. 모든 쉘이 열렸는지 확인 -> isAllCellOpened()
    * 2. 모든 쉘이 열렸으면 gameStatus 상태값 변환

## Section 2-7 추상화 레벨
`하나의 세계 안에서는, 추상화 레벨이 동등해야 한다.`

하나의 클래스에 메서드는 레벨이 10이라고 하고, 구현체는 5라고 했을 때 추상화 레벨이 다르면 읽기가 어려워 진다.

추상화 레벨을 맞춰줘야 읽기 편해진다.

### 기존
기존에는 showBoard()라는 추상화가 높은 메서드 다음에 구현체가 나온다.
읽기에 많이 불편하지는 않지만 조금 더 추상화 레벨을 맞춰준다면 메서드로 뺄 수 있다.

```java
            showBoard();
            if (gameStatus == GAME_CLEAR) {
                System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
                break;
            }
            if (gameStatus == GAME_OVER) {
                System.out.println("지뢰를 밟았습니다. GAME OVER!");
                break;
            }
```

### 변경
showBoard() 다음에 doseUserWinTheGame()과 doseUserLoseTheGame() 메서드를 만들어서 같은 추상화 레벨로 만들어줬다. 

```java
            showBoard();
            if (doseUserWinTheGame()) {
                System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
                break;
            }
            if (doseUserLoseTheGame()) {
                System.out.println("지뢰를 밟았습니다. GAME OVER!");
                break;
            }
```

## Section 2-8 매직 넘버, 매직 스트링
상수로 추출한다는 것의 의미는 이름을 주고 의미를 부여하는 추상화 행위이다.

### 매직 넘버, 매직 스트링
* 의미를 갖고 있으나, 상수로 추출되지 않은 숫자, 문자열 등
* 상수 추출로 이름을 짓고 의미를 부여함으로써 가독성, 유지보수성 향상

## Section 3-2 Early return
Early return으로 else의 사용을 지양

### 기존
기존 코드에서는 if, else if, else 구문이 혼재되어 있다.

```java
            int selectedColIndex = getSelectedColIndex(cellInput);
            int selectedRowIndex = getSelectedRowIndex(cellInput);
            if (doseUserChooseToPlantFlag(userActionInput)) {
                BOARD[selectedRowIndex][selectedColIndex] = FLAG_SIGN;
                checkIfGameIsClear();
            } else if (doseUserChooseToOpenCell(userActionInput)) {
                if (isLandMineCell(selectedRowIndex, selectedColIndex)) {
                    BOARD[selectedRowIndex][selectedColIndex] = LAND_MINE_SIGN;
                    changeGameStatusDoLose();
                    continue;
                } else {
                    open(selectedRowIndex, selectedColIndex);
                }
                checkIfGameIsClear();
            } else {
                System.out.println("잘못된 번호를 선택하셨습니다.");
```

### 변경
Early return을 사용하면 if, else-if, else 구문을 제거 가능하다.

```java
    private static void actOrCell(String cellInput, String userActionInput) {
        int selectedColIndex = getSelectedColIndex(cellInput);
        int selectedRowIndex = getSelectedRowIndex(cellInput);
        if (doseUserChooseToPlantFlag(userActionInput)) {
            BOARD[selectedRowIndex][selectedColIndex] = FLAG_SIGN;
            checkIfGameIsClear();
            return;
        }
        if (doseUserChooseToOpenCell(userActionInput)) {
            if (isLandMineCell(selectedRowIndex, selectedColIndex)) {
                BOARD[selectedRowIndex][selectedColIndex] = LAND_MINE_SIGN;
                changeGameStatusDoLose();
                return;
            }
            open(selectedRowIndex, selectedColIndex);
            checkIfGameIsClear();
            return;
        }
        System.out.println("잘못된 번호를 선택하셨습니다.");
    }
```
