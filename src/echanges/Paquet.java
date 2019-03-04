package echanges;

public class Paquet
{
    char[] data;

    public Paquet()
    {
        data = new char[0];
    }

    public Paquet(char[] data)
    {
        this.data = data;
    }

    public char[] getData()
    {
        return this.data;
    }

    public String getMsg()
    {
        StringBuilder strBui = new StringBuilder();
        strBui.append(data);
        return strBui.toString();
    }

    @Override
    public boolean equals(Object obj)
    {
        Paquet t = (Paquet) obj;
        if (data.length != t.data.length)
        {
            return false;
        }
        for (int i = 0; i < data.length; i++)
        {
            if (data[i] != t.data[i])
            {
                return false;
            }
        }
        return true;
    }

}
