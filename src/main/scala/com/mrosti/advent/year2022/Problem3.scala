package com.mrosti.advent.year2022
import com.mrosti.advent.AOC
import cats.*
import cats.implicits.*
import cats.effect.kernel.*
import fs2.*
import cats.data.NonEmptyList
import org.typelevel.log4cats.slf4j.Slf4jLogger

object Problem3 extends AOC("2022", "3"):

  object Priority:
    def apply(char: Char): Option[Int] =
      Option(char)
        .map(_.toInt)
        .map(t => Option.when(t >= 97)(t - 97).getOrElse(t - 65 + 26))
        .map(_ + 1)

  final case class Compartment(items: Seq[Char])

  object Compartment:
    def shared(seq: NonEmptyList[Compartment]): Option[Char] =
      seq.reduce((a, b) => Compartment(a.items.intersect(b.items))).items.headOption

  def solve[F[_]: Async](input: Stream[F, String], chunks: Int): F[String] = input
    .chunkN(chunks, false)
    .map(_.map(Compartment(_)))
    .map(NonEmptyList.fromFoldable)
    .map(_.flatMap(Compartment.shared).flatMap(Priority(_)))
    .flattenOption
    .compile
    .toList
    .map(_.sum.show)

  override def part1[F[_]: Async](input: Stream[F, String]): F[String] =
    solve(input.flatMap(rucksack => Stream.emits(rucksack.splitAt(rucksack.length() / 2).toList)), 2)

  override def part2[F[_]: Async](input: Stream[F, String]): F[Option[String]] =
    solve(input, 3).map(_.pure)
