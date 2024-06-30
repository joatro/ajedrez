package ajedrez.app

interface ChessDelegate {
    fun pieceAt(square: Square): ChessPiece?
          fun movePiece(from: Square, to: Square)
}

