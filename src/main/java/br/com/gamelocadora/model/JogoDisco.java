
package br.com.gamelocadora.model;

import java.math.BigDecimal;

public class JogoDisco extends Midia {

    public JogoDisco(
        String t,
        String p,
        String g,
        BigDecimal v
    ) {
        super(t, p, g, v);
    }

    public JogoDisco(
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

        return "Mídia em disco";
    }
}
