/*
 * Copyright 2021 Marcus Rosti
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mrosti.advent.year2022

import com.mrosti.advent.AOC
import cats.implicits.*
import cats.effect.implicits.*
import cats.effect.kernel.Async
import fs2.*
import cats.Applicative

object Problem7 extends AOC("2022", "7"):

  val init: Map[Directory, Seq[FileObject]] = Map.empty

  sealed trait FileObject(name: String)

  final case class File(name: String, size: Long)                    extends FileObject(name)
  final case class Directory(name: String, objects: Seq[FileObject]) extends FileObject(name)

  sealed trait Command
  case class CD(directory: String)
  case object LS

  override def part1[F[_]](input: Stream[F, String])(using Async[F]): F[String] =
    Async[F].pure("")
  override def part2[F[_]](input: Stream[F, String])(using Async[F]): F[Option[String]] =
    Async[F].pure(Option.empty)
