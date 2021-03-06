package com.eharmony.aloha.models

import com.eharmony.aloha.reflect._
import com.eharmony.aloha.semantics.Semantics
import com.eharmony.aloha.semantics.func.GenFunc0

import scala.util.Try

/**
  * Created by jmorra on 2/29/16.
  */
object AnySemanticsWithoutFunctionCreation extends Semantics[Any] {
  def refInfoA = RefInfo[Any]
  def accessorFunctionNames = Nil
  def close() {}
  def createFunction[B: RefInfo](codeSpec: String, default: Option[B]) = {
    val right = Try {
      val long = codeSpec.toLong
      Right(GenFunc0(codeSpec, (a: Any) => long.asInstanceOf[B]))
    }
    right.getOrElse(Left(Seq("createFunction not supported.")))
  }
}
