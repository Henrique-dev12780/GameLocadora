
package br.com.gamelocadora.model;

import java.math.BigDecimal;

public class JogoCartucho extends Midia {

    public JogoCartucho(
        String t,
        String p,
        String g,
        BigDecimal v
    ) {
        super(t, p, g, v);
    }

    public JogoCartucho(
        Long id,
        String t,
        String p,
        String g,
        BigDecimal v,
        boolean a
    ) {
        super(id, t, p, g, v, a);
    }

    @Override
    public String descricaoFormato() {

        return "Mídia em cartucho";
    }
}
