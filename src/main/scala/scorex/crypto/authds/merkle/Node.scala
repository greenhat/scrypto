package scorex.crypto.authds.merkle

import scorex.crypto.authds.LeafData
import scorex.crypto.hash._
import scorex.utils.ScryptoLogging

trait Node[D <: Digest] extends ScryptoLogging {
  def hash: D
}

case class InternalNode[D <: Digest](left: Node[D], right: Node[D])
                                    (implicit val hf: CryptographicHash[D]) extends Node[D] {

  override lazy val hash: D = hf.prefixedHash(MerkleTree.InternalNodePrefix, left.hash, right.hash)

  override def toString: String = s"InternalNode(" +
    s"left: ${encoder.encode(left.hash)}, " +
    s"right: ${encoder.encode(right.hash)}," +
    s"hash: ${encoder.encode(hash)})"
}

case class Leaf[D <: Digest](data: LeafData)(implicit val hf: CryptographicHash[D]) extends Node[D] {
  override lazy val hash: D = hf.prefixedHash(MerkleTree.LeafPrefix, data)

  override def toString: String = s"Leaf(${encoder.encode(hash)})"
}

case class EmptyNode[D <: Digest]() extends Node[D] {
  override val hash: D = Array[Byte]().asInstanceOf[D]
}
