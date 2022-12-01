package com.mrosti.advent.year2022

import com.mrosti.advent.year2021.AOC
import cats.implicits._
import cats.effect.kernel._
import fs2._
import org.typelevel.log4cats.slf4j.Slf4jLogger

object Problem1 extends AOC("2022", "1"):


  private def solution[F[_]: Async](input: Stream[F, String], topK: Int): F[String] =
    input
        .split(_.isBlank())
        .map(_.map(_.toLong).foldLeft(0l)(_ + _))
        .compile
        .toList
        .map(_.sorted.reverse.take(topK).sum.show)


  override def part1[F[_]: Async](input: Stream[F, String]): F[String] = 
    solution(input, 1)

  override def part2[F[_]: Async](input: Stream[F, String]): F[Option[String]] = 
    solution(input, 3).map(_.some)
