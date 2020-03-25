from pydantic import BaseModel


class A(BaseModel):
    a: str

    def vali<caret>date_a(cls):
        pass
