import requests;
import json;
import os;
#import github;
#import datetime;
from datetime import date
from datetime import datetime
from dateutil.relativedelta import relativedelta

#some resources for reference:
#https://stackoverflow.com/questions/27931139/how-to-use-github-v3-api-to-get-commit-count-for-a-repo

# Make sure to set your own GITHUBTOKEN env key!
token = "token " + os.getenv('GITHUBTOKEN')


def getGithubCommits(repo = "MockResearch", user = "redroach51"):
    commits_orig = requests.get('https://api.github.com/repos/{user}/{repo}/commits?simple=yes&per_page=100&page=1'
                                    .format(user=user, repo=repo),
                                headers={"Authorization": token})
    commits = commits_orig.json();
    
    while 'next' in commits_orig.links.keys():
        commits_orig = requests.get(commits_orig.links['next']['url'],
                        headers={"Authorization": token})
        commits.extend(commits_orig.json())
     
#   examineJSON(commits);
    return commits;

def getGithubContributors(repo = "MockResearch", user = "redroach51"):
    
    contrib_orig = requests.get('https://api.github.com/repos/{user}/{repo}/contributors?simple=yes&per_page=100&page=1'
                                    .format(user=user, repo=repo),
                                headers={"Authorization": token})
    contributors = contrib_orig.json();

    while 'next' in contrib_orig.links.keys():
        contrib_orig = requests.get(contrib_orig.links['next']['url'],
                        headers = {"Authorization": token});
        contributors.extend(contrib_orig.json())
    
    contrib_list = []
    for contributor in contributors:
        contrib_list.append([contributor["login"],contributor["contributions"]])
        
#    examineJSON(contributors)    
    return contrib_list        

def getGithubRepo(repo = "MockResearch", user = "redroach51"):
    repository = requests.get('https://api.github.com/repos/{user}/{repo}'
                                    .format(user=user, repo=repo),
                                headers={"Authorization": token})
    return repository.json()
    
def getGithubReleases(repo = "MockResearch", user = "redroach51"):
    repository = requests.get('https://api.github.com/repos/{user}/{repo}/releases'
                                    .format(user=user, repo=repo),
                                headers={"Authorization": token})
    return repository.json()


def getCommitStatistics(commits):
    
#    examineJSON(commits[0])
    
    dates = [commits[0]["commit"]["author"]["date"],commits[-1]["commit"]["author"]["date"]]
    dates[0] = datetime.strptime(dates[0],"%Y-%m-%dT%H:%M:%SZ")
    dates[1] = datetime.strptime(dates[1],"%Y-%m-%dT%H:%M:%SZ")
    length = len(commits)
    
    avg_date = (dates[0] - dates[1]) / length
    return [length,avg_date]

def getContributorStatistics(contributors):
    contrib_list = []
    lurker_count = 0
    
    for contributor in contributors:
        contrib_list.append(contributor[1])
    
    top_contrib = max(contrib_list)
    for num in contrib_list:
        if num < top_contrib / 10:
            lurker_count += 1
    
    return [len(contrib_list),lurker_count,max(contrib_list)]

def getReleaseStatistics(releases):
    
#    examineJSON(releases)
    if releases == []:
        return ["0","N/A"]
    
    dates = [releases[0]["created_at"],releases[-1]["created_at"]]
    dates[0] = datetime.strptime(dates[0],"%Y-%m-%dT%H:%M:%SZ")
    dates[1] = datetime.strptime(dates[1],"%Y-%m-%dT%H:%M:%SZ")
    length = len(releases)
    
    avg_date = (dates[0] - dates[1]) / length
    return [length,avg_date]

def examineJSON(json_object):
   with open('TempOutput.txt','w') as file:
       json.dump(json_object,file)

def main():
    user_name = input("Input GitHub repo's owner: ")
    user_repo = input("Input GitHub repo's name: ")
    if (user_name == "" or user_repo == ""):
        print("Default test API set to creator RedRoach51's host repo.\n")
        user_name = "redroach51"
        user_repo = "MockResearch"

    commits = getGithubCommits(user_repo,user_name)
    commits_stats = getCommitStatistics(commits)
    print("\n{userRepo} Commits: {numCommits}".format(userRepo=user_repo, numCommits=commits_stats[0]))
    print("Avg time between commits: " + str(commits_stats[1]) + "\n")
    

    contributors = getGithubContributors(user_repo,user_name)
    contributor_stats = getContributorStatistics(contributors)

    print("Contributors ({total_contributors}):"
          .format(total_contributors=contributor_stats[0]))    
#debug    for contributor in contributors:
#        print('{contributor}: {contributions}'
#                  .format(contributor=contributor[0], contributions=contributor[1]))
    print("Total Lurkers (< 10% * {max_contribution}) : {lurker_count}\n"
              .format(max_contribution = contributor_stats[2], lurker_count = contributor_stats[1]))
    
    repo = getGithubRepo(user_repo,user_name)
    print("Repository Size (KB) : {repoSize}".format(repoSize=repo['size']))

    releases = getGithubReleases(user_repo,user_name)
    releases_stats = getReleaseStatistics(releases);
    print("\n{userRepo} Releases: {numReleases}".format(userRepo=user_repo, numReleases=releases_stats[0]))
    print("Avg time between releases: " + str(releases_stats[1]) + "\n")

    

if __name__ == '__main__':
    main()