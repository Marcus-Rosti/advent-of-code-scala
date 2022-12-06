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

package com.mrosti.advent

import cats.implicits.*
import cats.effect.*
import cats.effect.implicits.*
import cats.effect.IO.*
import cats.effect.std.Env

import org.typelevel.log4cats.slf4j.*
import com.mrosti.advent.year2022.*
import scala.concurrent.duration

object Main extends IOApp.Simple:

  val solutions: IO[Seq[Unit]] = Seq(
    Problem1(),
    Problem2(),
    Problem3(),
    Problem4(),
    Problem5()
  ).traverse(identity(_))

  override def run: IO[Unit] =
    for {
      logger    <- Slf4jFactory.create[IO]
      _         <- logger.info("Starting!")
      (time, _) <- Clock[IO].timed(solutions)
      _         <- logger.info(s"Finished!! in ${time.toMillis}ms")
    } yield ()
