def process(params):
    keys = params.split(',')
    for key in keys:
        key_down(key)
    for key in keys:
        key_up(key)
process(params)