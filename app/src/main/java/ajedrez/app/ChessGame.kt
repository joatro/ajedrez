package ajedrez.app



import Ajedreze.App.R
import kotlin.math.abs

object ChessGame : ChessDelegate {


    private var piecesBox = mutableListOf<ChessPiece>()

    init {
        reset()
    }

    private fun clear() {
        piecesBox.clear()
    }

    private fun addPiece(piece: ChessPiece) {
        piecesBox.add(piece)
    }

    private fun canKnightMove(from: Square, to: Square): Boolean {
        return abs(from.col - to.col) == 2 && abs(from.row - to.row) == 1 ||
                abs(from.col - to.col) == 1 && abs(from.row - to.row) == 2
    }

    private fun canRookMove(from: Square, to: Square): Boolean {
        return from.col == to.col && isClearVerticallyBetween(from, to) ||
                from.row == to.row && isClearHorizontallyBetween(from, to)
    }

    private fun isClearVerticallyBetween(from: Square, to: Square): Boolean {
        if (from.col != to.col) return false
        val gap = abs(from.row - to.row) - 1
        if (gap == 0 ) return true
        for (i in 1..gap) {
            val nextRow = if (to.row > from.row) from.row + i else from.row - i

            if (pieceAt(Square(from.col, nextRow)) != null){
                return false
            }
        }
        return true
    }



    private fun isClearHorizontallyBetween(from: Square, to: Square): Boolean {
        if (from.row != to.row) return false
        val gap = abs(from.col - to.col) - 1
        if (gap == 0 ) return true
        for (i in 1..gap) {
            val nextCol = if (to.col > from.col) from.col + i else from.col - i
            if (pieceAt(Square(nextCol, from.row)) != null) {
                return false
            }
        }
        return true
    }

    private fun isClearDiagonally(from: Square, to: Square): Boolean {
        if (abs(from.col - to.col) != abs(from.row - to.row)) return false
        val gap = abs(from.col - to.col) - 1
        for (i in 1..gap) {
            val nextCol = if (to.col > from.col) from.col + i else from.col - i
            val nextRow = if (to.row > from.row) from.row + i else from.row - i
            if (pieceAt(nextCol, nextRow) != null) {
                return false
            }
        }
        return true
    }

    private fun canBishopMove(from: Square, to: Square): Boolean {
        if (abs(from.col - to.col) == abs(from.row - to.row)) {
            return isClearDiagonally(from, to)
        }
        return false
    }

    private fun canQueenMove(from: Square, to: Square): Boolean {
        return canRookMove(from, to) || canBishopMove(from, to)
    }

    private fun canKingMove(from: Square, to: Square): Boolean {
        if (canQueenMove(from, to)) {
            val deltaCol = abs(from.col - to.col)
            val deltaRow = abs(from.row - to.row)
            return deltaCol == 1 && deltaRow == 1 || deltaCol + deltaRow == 1
        }
        return false
    }

    //lo es para los peones hacia abajo y hacia arriba
    /*
    private fun canPawnMove(from: Square, to: Square): Boolean {
         if (from.col == to.col) {
             if (from.row == 1) {
                 return to.row == 2 || to.row == 3
             } else if (from.row == 6) {
                 return to.row == 5 || to.row == 4
             }
         }
         return false
     }*/
    private fun canPawnMove(from: Square, to: Square): Boolean {
        val movingPiece = pieceAt(from) ?: return false
        return when (movingPiece.player) {
            Player.WHITE -> {
                // Los peones blancos se mueven hacia arriba en el tablero (incrementando la fila)
                from.col == to.col && to.row == from.row + 1
            }
            Player.BLACK -> {
                // Los peones negros se mueven hacia abajo en el tablero (disminuyendo la fila)
                from.col == to.col && to.row == from.row - 1
            }
        }
    }

    private fun canMove(from: Square, to: Square): Boolean {
        if (from.col == to.col && from.row == to.row) {
            return  false
        }
        val movingPiece = pieceAt(from) ?: return false
        return when(movingPiece.chessman) {
            Chessman.KNIGHT -> canKnightMove(from, to)
            Chessman.ROOK -> canRookMove(from, to)
            Chessman.BISHOP -> canBishopMove(from, to)
            Chessman.QUEEN -> canQueenMove(from, to)
            Chessman.KING -> canKingMove(from, to)
            Chessman.PAWN -> canPawnMove(from, to)
        }
    }


    private fun reset() {
        clear()

        for (i in 0 until 2) {
            addPiece(ChessPiece( 0 + i * 7, 0, Player.WHITE, Chessman.ROOK, R.drawable.rook_white))
            addPiece(ChessPiece(0 + i * 7, 7, Player.BLACK, Chessman.ROOK, R.drawable.rook_black))

            addPiece(ChessPiece(1 + i * 5, 0, Player.WHITE, Chessman.KNIGHT, R.drawable.knight_white))
            addPiece(ChessPiece(1 + i * 5, 7, Player.BLACK, Chessman.KNIGHT, R.drawable.knight_black))

            addPiece(ChessPiece(2 + i * 3, 0, Player.WHITE, Chessman.BISHOP, R.drawable.bishop_white))
            addPiece(ChessPiece(2 + i * 3, 7, Player.BLACK, Chessman.BISHOP, R.drawable.bishop_black))
        }

        for (i in 0 until 8) {
            addPiece(ChessPiece(i, 1, Player.WHITE, Chessman.PAWN, R.drawable.pawn_white))
            addPiece(ChessPiece(i, 6, Player.BLACK, Chessman.PAWN, R.drawable.pawn_black))
        }

        addPiece(ChessPiece(3, 0, Player.WHITE, Chessman.QUEEN, R.drawable.queen_white))
        addPiece(ChessPiece(3, 7, Player.BLACK, Chessman.QUEEN, R.drawable.queen_black))
        addPiece(ChessPiece(4, 0, Player.WHITE, Chessman.KING, R.drawable.king_white))
        addPiece(ChessPiece(4, 7, Player.BLACK, Chessman.KING, R.drawable.king_black))
    }

    override fun pieceAt(square: Square): ChessPiece? {
        return pieceAt(square.col, square.row)
    }
    private fun isCheckMate(player: Player): Boolean {
        val king = piecesBox.find { it.player == player && it.chessman == Chessman.KING } ?: return false
        val kingSquare = Square(king.col, king.row)

        // Si el rey no está en jaque, no puede ser jaque mate
        if (!isInCheck(kingSquare, player)) {
            return false
        }

        // Comprueba todos los movimientos posibles del rey para ver si puede moverse a una casilla segura
        for (row in 0 until 8) {
            for (col in 0 until 8) {
                val toSquare = Square(col, row)
                if (canMove(kingSquare, toSquare) && !isInCheck(toSquare, player)) {
                    return false
                }
            }
        }

        // Comprueba todos los movimientos posibles de todas las otras piezas del jugador para ver si pueden bloquear el jaque
        for (piece in piecesBox) {
            if (piece.player == player && piece.chessman != Chessman.KING) {
                for (row in 0 until 8) {
                    for (col in 0 until 8) {
                        val fromSquare = Square(piece.col, piece.row)
                        val toSquare = Square(col, row)
                        if (canMove(fromSquare, toSquare)) {
                            // Realiza el movimiento y comprueba si el rey sigue en jaque

                            val capturedPiece = pieceAt(toSquare)
                            movePiece(fromSquare, toSquare)
                            val stillInCheck = isInCheck(kingSquare, player)
                            // Deshace el movimiento
                            movePiece(toSquare, fromSquare)
                            capturedPiece?.let { piecesBox.add(it) }
                            if (!stillInCheck) {
                                return false
                            }
                        }
                    }
                }
            }
        }

        // Si ninguna de las piezas puede moverse a una casilla que bloquee el jaque, es jaque mate
        return true
    }

    private fun isInCheck(kingSquare: Square, player: Player): Boolean {
        for (piece in piecesBox) {
            if (piece.player != player && canMove(Square(piece.col, piece.row), kingSquare)) {
                return true
            }
        }
        return false
    }
    override fun movePiece(from: Square, to: Square) {
        if (canMove(from, to)) {
            val movingPiece = pieceAt(from)
            val targetPiece = pieceAt(to)

            // Si la casilla de destino está ocupada por una pieza enemiga, la eliminamos
            if (targetPiece != null && targetPiece.player != movingPiece?.player) {
                piecesBox.remove(targetPiece)
            }

            // Mueve la pieza
            movingPiece?.let {
                it.col = to.col
                it.row = to.row
            }
            if (isCheckMate(Player.BLACK)) {

                GameListener.onGameEnd()
            }else if (isCheckMate(Player.WHITE)){
                GameListener.onGameEnd()
            }
        }
    }
    private fun pieceAt(col: Int, row: Int): ChessPiece? {
        for (piece in piecesBox) {
            if (col == piece.col && row == piece.row) {
                return  piece
            }
        }
        return null
    }

    override fun toString(): String {
        var desc = " \n"
        for (row in 7 downTo 0) {
            desc += "$row"
            desc += boardRow(row)
            desc += "\n"
        }
        desc += "  0 1 2 3 4 5 6 7"

        return desc
    }

    private fun boardRow(row: Int) : String {
        var desc = ""
        for (col in 0 until 8) {
            desc += " "
            desc += pieceAt(col, row)?.let {
                val white = it.player == Player.WHITE
                when (it.chessman) {
                    Chessman.KING -> if (white) "k" else "K"
                    Chessman.QUEEN -> if (white) "q" else "Q"
                    Chessman.BISHOP -> if (white) "b" else "B"
                    Chessman.ROOK -> if (white) "r" else "R"
                    Chessman.KNIGHT -> if (white) "n" else "N"
                    Chessman.PAWN -> if (white) "p" else "P"
                }
            } ?: "."
        }
        return desc
    }
}