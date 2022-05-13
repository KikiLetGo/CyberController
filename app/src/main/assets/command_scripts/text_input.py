def text_input(params):
    pyperclip.copy(params)
    key_down("ctrl")
    key_down("v")
    key_up("v")
    key_up("ctrl")
text_input(params)