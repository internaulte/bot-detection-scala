package core

import org.mockito.MockitoSugar
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funspec.AsyncFunSpec

trait MockitoUnitTestSpec extends AsyncFunSpec with MockitoSugar with BeforeAndAfterAll
