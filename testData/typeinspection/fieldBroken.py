import typic
from builtins import *


@typic.klass
class A:
    a<EOLError descr="Expression expected"></EOLError>
    b =<EOLError descr="Expression expected"></EOLError>
    d:<EOLError descr="expression expected"></EOLError>
    e:<error descr="expression expected"> </error> = '123'

A(a=int(123), b=str('123'), c=str('456'), d=str('789'))
