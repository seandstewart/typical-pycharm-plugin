import typic
from builtins import *


@typic.klass
class A:
    a: str


A(<warning descr="Expected type 'str', got 'int' instead">int(123)</warning>)
