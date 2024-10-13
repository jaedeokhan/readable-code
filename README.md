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
