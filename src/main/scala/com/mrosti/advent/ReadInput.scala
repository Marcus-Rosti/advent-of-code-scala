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

import cats._
import cats.effect._
import cats.syntax.all._
import fs2._
import fs2.text
import fs2.io.file._

object ReadInput {

  def apply[F[_]: Async](name: Path): Stream[F, String] =
    Files[F].readAll(name).through(text.utf8.decode).through(text.lines).filterNot(_.isBlank)
}
