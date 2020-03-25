import typic
from builtins import *


@typic.klass
class A:
    a: int = typic.field(int(123))
    b = typic.field(123)
    c = typic.field(default=int(123))
    d: int = typic.field(...)


A(a=int(123), b=int(123), c=int(123), d=int(123))
