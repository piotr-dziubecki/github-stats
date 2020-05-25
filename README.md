# github-stats
example how to get various stats out of github

## What does it do ?
The script queries for all public repositories of given user/project. Next it iterates over each repository and lists number of contributors. At the end it writes the data to the csv file.

## Prerequisites 

We need to generate user oauth token:
https://help.github.com/en/github/authenticating-to-github/creating-a-personal-access-token-for-the-command-line

You don't need to select anything for permissions. Make sure you copy the token since it is displayed only once. 

By using oauth toke we increase the query limits on API.

After the token is generated it needs to be added to auth.properties

# Usage

`groovy ghstats.groovy <user/project>`

# Example run

## Command line
```
groovy ghstats.groovy deepres

Querying: http://api.github.com/users/deepres/repos
Repo: 30breaksaver contributors: 1
Repo: 30breaksaver-ng contributors: 1
Repo: dotfiles contributors: 1
Repo: github-stats contributors: 1
Repo: grails-doc contributors: 30
Repo: OffsetDateTime-with-RestTemplate contributors: 1
Logins succesfully exported to: deepres.csv file
You have 4901 free requests left for next 60 minutes
```

## CSV file contents

```
cat deepres.csv

login, html-link
deepres,https://github.com/deepres
graemerocher,https://github.com/graemerocher
jeffbrown,https://github.com/jeffbrown
pledbrook,https://github.com/pledbrook
burtbeckwith,https://github.com/burtbeckwith
bobbywarner,https://github.com/bobbywarner
anshbansal,https://github.com/anshbansal
lhotari,https://github.com/lhotari
jameskleeh,https://github.com/jameskleeh
fordguo,https://github.com/fordguo
ldaley,https://github.com/ldaley
rlovtangen,https://github.com/rlovtangen
lucastex,https://github.com/lucastex
jccorp,https://github.com/jccorp
doelleri,https://github.com/doelleri
and-dmitry,https://github.com/and-dmitry
B5A7,https://github.com/B5A7
yamkazu,https://github.com/yamkazu
marcpalmer,https://github.com/marcpalmer
jbwmcarter,https://github.com/jbwmcarter
mjcmatrix,https://github.com/mjcmatrix
micfra,https://github.com/micfra
jmurciego,https://github.com/jmurciego
pap,https://github.com/pap
alexanderzeillinger,https://github.com/alexanderzeillinger
tomaslin,https://github.com/tomaslin
joelwreed,https://github.com/joelwreed
juarezmax,https://github.com/juarezmax
rosenfeld,https://github.com/rosenfeld
kgeis,https://github.com/kgeis
ZacharyKlein,https://github.com/ZacharyKlein
```




