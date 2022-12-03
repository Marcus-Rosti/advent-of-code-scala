package com.mrosti.advent.year2022
import com.mrosti.advent.AOC
import cats._
import cats.implicits._
import cats.effect.kernel._
import fs2._
import cats.data.NonEmptyList
import org.typelevel.log4cats.slf4j.Slf4jLogger

object Problem3 extends AOC("2022", "3"):

  object Priority:
    def apply(char: Char): Option[Int] = 
        Option(char)
          .map(_.toInt)
          // .filterNot(t => t > 122 || t < 65)
          .map(t => Option.when(t >= 97)(t - 97).getOrElse(t - 65 +26))
          .map(_ + 1)

  final case class Compartment(items: Seq[Char])

  object Compartment:
    def shared(seq: NonEmptyList[Compartment]): Option[Char] =
      seq.reduce((a, b) => Compartment(a.items.intersect(b.items))).items.headOption

  final case class RuckSack(compartments: NonEmptyList[Compartment]) 

  def solve[F[_]: Async](input: Stream[F, Seq[String]]): F[String] = input
      .map(_.map(Compartment(_)))
      .map(chunks => NonEmptyList.fromFoldable(chunks).map(RuckSack(_)))
      .flattenOption
      .map(r => Compartment.shared(r.compartments))
      .map(_.flatMap(Priority(_)))
      .flattenOption
      .compile
      .toList
      .map(_.sum.show)
  

  override def part1[F[_]: Async](input: Stream[F, String]): F[String] = 
    solve(input.map(rucksack => rucksack.splitAt(rucksack.length()/2).toList.toSeq))


  override def part2[F[_]: Async](input: Stream[F, String]): F[Option[String]] =     
      solve(input
        .chunkN(3, false)
        .map(_.toList))
        .map(_.pure)

