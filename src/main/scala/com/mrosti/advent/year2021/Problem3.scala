package com.mrosti.advent.year2021

import cats.effect._
import cats.implicits._
import fs2._

object Problem3 extends AOC("2021", "3"):
    override def part1[F[_]: Async](input: fs2.Stream[F, String]): F[String] =
        Async[F].pure("")

    override def part2[F[_]: Async](input: fs2.Stream[F, String]): F[Option[String]] = 
        Async[F].pure(Option.empty)


