package com.soen6441.warzone.service.impl;


import com.soen6441.warzone.model.CommandResponse;
import com.soen6441.warzone.model.GamePlay;
import com.soen6441.warzone.model.Player;
import com.soen6441.warzone.service.GameConfigService;
import com.soen6441.warzone.service.GeneralUtil;
import com.soen6441.warzone.service.MapHandlingInterface;
import org.springframework.beans.factory.annotation.Autowired;
import com.soen6441.warzone.service.impl.MapHandlingImpl;
import com.soen6441.warzone.model.*;
import org.springframework.stereotype.Service;

import java.io.LineNumberInputStream;
import java.util.*;
import java.util.Random;
/** this is an implementation of GameConfigService for confihuration utility
 *
 * @author <a href="mailto:y_vaghan@encs.concordia.ca">Yashkumar Vaghani</a>
 */

@Service
public class GameConfigServiceImpl implements GameConfigService {

    @Autowired
    private GeneralUtil d_generelUtil;

    @Autowired
    private GamePlay d_gamePlay;

    @Autowired
    MapHandlingImpl d_mapConfig;

    @Override
    public CommandResponse showPlayerMap() {

        CommandResponse l_showCountris=d_mapConfig.showMap(d_warMap);
        if(d_gamePlay.getPlayerList()==null)
        {
            d_generelUtil.prepareResponse(false,"no player are present currently");
        }
        else
        {

                String l_showMapOfCountris=l_showCountris.getD_responseString();
                List<Country> l_countryList=d_mapConfig.getAvailableCountries(d_warMap);
                int l_colSize=l_countryList.size()+1;
                int l_rowSize=d_gamePlay.getPlayerList().size()+1;
                String [][]l_playerToCountry=new String[l_rowSize][l_colSize];
                for(int l_i=0;l_i<l_rowSize;l_i++)
                {
                    for(int l_j=0;l_j<l_colSize;l_j++)
                    {
                        if(l_i==0 && l_j==0)
                        {
                            continue;
                        }
                        else if(l_i==0 && l_j!=0)
                        {
                            l_playerToCountry[l_i][l_j]=l_countryList.get(l_j-1).getD_countryName();
                        }
                        else if(l_i!=0 && l_j==0)
                        {
                            l_playerToCountry[l_i][l_j]=d_gamePlay.getPlayerList().get(l_i-1).getD_playerName();
                        }
                        else
                        {
                            if(d_gamePlay.getPlayerList().get(l_i-1).getD_ownedCountries().contains(l_countryList.get(l_j-1)))
                            {
                                //l_playerToCountry[l_i][l_j]= String.valueOf(l_countryList.get(l_j-1).getD_noOfArmies());
                                l_playerToCountry[l_i][l_j]="1";


                            }
                            else
                            {
                                l_playerToCountry[l_i][l_j]="0";
                            }
                        }
                    }
                }
            System.out.println(l_showMapOfCountris);
            for (int i=0;i<l_rowSize;i++)
            {
                for(int j=0;j<l_colSize;j++)
                {
                    System.out.println(l_playerToCountry[i][j]);
                }
                System.out.println();
            }

        }


        return l_showCountris;
        
    }

    @Override
    public CommandResponse assignCountries() {

        if(d_gamePlay.getPlayerList()==null)
        {
            d_generelUtil.prepareResponse(false,"No player in the game");

        }
        else if(d_gamePlay.getPlayerList().get(0).getD_ownedCountries().size()!=0)
        {
            d_generelUtil.prepareResponse(false,"Countries are already assigned");
        }
        else {
            int l_noOfPlayers=d_gamePlay.getPlayerList().size();

            int l_noOfCountries=d_mapConfig.getAvailableCountries(d_warMap).size();
            List<Country> l_country_check=d_mapConfig.getAvailableCountries(d_warMap);
            List<Player> l_players=d_gamePlay.getPlayerList();
            int l_counter=l_noOfCountries/l_noOfPlayers;
            int l_modulus=l_noOfCountries%l_noOfPlayers;
            List<Country> l_checkCountryOnPlayer=new ArrayList<Country>();
            int l_i=0,l_j=0;
            for(l_i=0;l_i<l_counter;l_i++)
            {
                for(l_j=0;l_j<l_noOfPlayers;l_j++)
                {
                    if(d_gamePlay.getPlayerList().get(l_j).getD_ownedCountries()==null)
                    {
                        List<Country> l_country=new ArrayList<Country>();
                        while(true)
                        {Random rand = new Random();
                            int rand_int1 = rand.nextInt(l_noOfCountries);
                            if(!l_checkCountryOnPlayer.contains(l_country_check.get(rand_int1)))
                            {
                                l_country.add(l_country_check.get(rand_int1));
                                l_checkCountryOnPlayer.add(l_country_check.get(rand_int1));
                                d_gamePlay.getPlayerList().get(l_j).setD_ownedCountries(l_country);
                                break;

                            }

                        }
                    }
                    else
                    {
                        while(true)
                        {Random rand = new Random();
                            int rand_int1 = rand.nextInt(l_noOfCountries);
                            if(!l_checkCountryOnPlayer.contains(l_country_check.get(rand_int1)))
                            {
                                d_gamePlay.getPlayerList().get(l_j).getD_ownedCountries().add(l_country_check.get(rand_int1));
                                l_checkCountryOnPlayer.add(l_country_check.get(rand_int1));
                                break;

                            }

                        }
                    }

                }
            }
            for(l_i=0;l_i<l_modulus;l_i++)
            {
                while(true)
                {Random rand = new Random();
                    int rand_int1 = rand.nextInt(l_noOfCountries);
                    if(!l_checkCountryOnPlayer.contains(l_country_check.get(rand_int1)))
                    {
                        d_gamePlay.getPlayerList().get(l_i).getD_ownedCountries().add(l_country_check.get(rand_int1));
                        l_checkCountryOnPlayer.add(l_country_check.get(rand_int1));
                        break;

                    }

                }
            }
            String l_response="";
            for(l_i=0;l_i<l_noOfPlayers;l_i++)
            {
                l_response=l_response+d_gamePlay.getPlayerList().get(l_i).getD_playerName()+" owns [";
                for(l_j=0;l_j<d_gamePlay.getPlayerList().get(l_i).getD_ownedCountries().size();l_j++)
                {
                    if(l_j==(d_gamePlay.getPlayerList().get(l_i).getD_ownedCountries().size()-1))
                    {
                        l_response=l_response+d_gamePlay.getPlayerList().get(l_i).getD_ownedCountries().get(l_j).getD_countryName();
                    }
                    else
                    {
                        l_response=l_response+d_gamePlay.getPlayerList().get(l_i).getD_ownedCountries().get(l_j).getD_countryName()+",";
                    }

                }
                l_response=l_response+"]\n";
            }

            System.out.println(l_response);
            d_generelUtil.prepareResponse(true,l_response);
        }







        return d_generelUtil.getResponse();

    }

}
